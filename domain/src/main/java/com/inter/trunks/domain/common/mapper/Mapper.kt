package com.inter.trunks.domain.common.mapper

interface Mapper<T, E> {
    fun mapFrom(from: T): E

    fun mapTo(from: E): T

    fun mapListFrom(from: List<T>): List<E> =
        from.map { mapFrom(it) }

    fun mapListTo(from: List<E>): List<T> =
        from.map { mapTo(it) }
}