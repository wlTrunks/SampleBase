package com.inter.trunks.data.common.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: T): Long

    @Insert(onConflict = REPLACE)
    suspend fun insert(items: Array<T>): Array<Long>

    @Update(onConflict = REPLACE)
    suspend fun update(item: T): Int

    @Delete
    suspend fun delete(item: T): Int

    @Delete
    suspend fun delete(items: Array<T>): Int

}