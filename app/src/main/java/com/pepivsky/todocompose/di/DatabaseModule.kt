package com.pepivsky.todocompose.di

import android.content.Context
import androidx.room.Room
import com.pepivsky.todocompose.data.ToDoDatabase
import com.pepivsky.todocompose.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // provee una instancia singleton de la base de datos
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ToDoDatabase::class.java, DATABASE_NAME).build()

    // provee la interface para hacer operaciones con la bd
    @Singleton
    @Provides
    fun provideDao(database: ToDoDatabase) = database.toDoDAO()
}