package com.example.proyect_room.di

import android.content.Context
import androidx.room.Room
import com.example.proyect_room.data.local.AppDatabase
import com.example.proyect_room.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "room_form_db"
        ).build()
    }

    @Provides
    fun provideUserDao(
        appDatabase: AppDatabase
    ): UserDao = appDatabase.userDao()
}
