package com.z.zcoreblocking.utils.jpa

import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import javax.persistence.criteria.*

class SelectQueryWrapper<T>(private val query: CriteriaQuery<T>,
                            private val root: Root<T>,
                            private val criteriaBuilder: CriteriaBuilder,
                            private val entityManager: EntityManager) {

    fun where(block: (root: Root<T>, cb: CriteriaBuilder) -> Predicate): SelectQueryWrapper<T> {
        val predicate = block(this.root, this.criteriaBuilder)
        this.query.where(predicate)
        return this
    }

    fun sort(block: (root: Root<T>, cb: CriteriaBuilder) -> List<Order>): SelectQueryWrapper<T> {
        val orderList: List<Order> = block(this.root, this.criteriaBuilder)
        this.query.orderBy(orderList)
        return this
    }

    fun build(): TypedQuery<T> {
        val select = this.query.select(this.root)
        return this.entityManager.createQuery(select)
    }
}