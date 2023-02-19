package com.qwk.chandrsekhar.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}

object DatabaseProvider {
    private var instance: MovieDatabase? = null

    fun getDatabase(context: Context): MovieDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java,
                "movies"

            )
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }
}
