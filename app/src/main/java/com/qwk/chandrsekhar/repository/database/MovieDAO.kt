package com.qwk.chandrsekhar.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("delete from movies where id=:id")
    suspend fun deleteMovie(id: Int)

    @Query("select * from movies")
    suspend fun getAllMovies() : List<MovieEntity>
}