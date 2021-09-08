package com.marcelo.conversor.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.marcelo.conversor.database.model.Exchange

@Dao
interface ExchangeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchange: Exchange)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchanges: List<Exchange>)

    @Query("SELECT * FROM Exchange")
    fun getAllExchanges() : LiveData<List<Exchange>>

    @Query("DELETE FROM Exchange")
    suspend fun clearExchanges()
}
