package com.z.zcoreblocking.utils.jpa

import javax.persistence.EntityManager

fun <T> EntityManager.select(clazz: Class<T>): SelectQueryWrapper<T> {
    val query = this.criteriaBuilder.createQuery(clazz)
    val root = query.from(clazz)
    return SelectQueryWrapper(query, root, this.criteriaBuilder, this)
}