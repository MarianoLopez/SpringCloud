package com.z.userservice.dao

import com.z.userservice.domain.User
import com.z.zcoreblocking.utils.jpa.select
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Path

@Repository
@Transactional
class UserRepositoryImpl(@PersistenceContext private val entityManager: EntityManager) : UserRepository {
    private val clazz = User::class.java
    private val criteriaBuilder = entityManager.criteriaBuilder
    private val fullEntityGraph = entityManager.getEntityGraph(User.USER_ROLES_GRAPH)

    override fun findAllByNameOrEmailContaining(nameOrEmail: String, pageable: Pageable): Page<User> {
        val like = "%${nameOrEmail.toUpperCase()}%"
        val countQuery = criteriaBuilder.createQuery(Long::class.java)
        val countSelect = countQuery.select(criteriaBuilder.count(countQuery.from(clazz)))

        val users = this.entityManager
                .select(clazz)
                .where { root, cb ->
                    cb.or(
                        cb.like(cb.upper(root["name"]), like),
                        cb.like(cb.upper(root["email"]), like)
                    ).apply { countQuery.where(this) }
                }
                .sort { root, cb ->
                    pageable.sort.map {
                        val prop: Path<User> = root[it.property]
                        if (it.isAscending) cb.asc(prop) else cb.desc(prop)
                    }.toList()
                }
                .build()
                .apply {
                    setHint("javax.persistence.fetchgraph", fullEntityGraph)
                    firstResult = pageable.pageNumber * pageable.pageSize
                    maxResults = pageable.pageSize
                }

        return PageImpl(users.resultList, pageable, entityManager.createQuery(countSelect).singleResult)
    }
}