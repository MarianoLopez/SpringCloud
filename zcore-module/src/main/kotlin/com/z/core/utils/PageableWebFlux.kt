package com.z.core.utils

import org.springframework.data.domain.Sort

typealias SortList = List<String>?

fun SortList.toSort(delimiter:String = "|"): Sort {
    return Sort.by(this?.map {
        if (it.contains(delimiter)) {
            val attributeAndOrder = it.split(delimiter)
            val attribute = attributeAndOrder.first()
            if (attributeAndOrder.last().trim().toUpperCase() == Sort.Direction.DESC.name) {
                Sort.Order.desc(attribute)
            } else {
                Sort.Order.asc(attribute)
            }
        } else {
            Sort.Order.by(it)
        }
    }?.toList() ?: emptyList())
}