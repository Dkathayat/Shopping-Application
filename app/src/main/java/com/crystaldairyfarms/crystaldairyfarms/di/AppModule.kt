package com.crystaldairyfarms.crystaldairyfarms.di

import android.content.Context
import androidx.room.Room
import com.crystaldairyfarms.crystaldairyfarms.data.room.CartDao
import com.crystaldairyfarms.crystaldairyfarms.data.room.CartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCartDatabase(@ApplicationContext context: Context): CartDatabase =
        Room.databaseBuilder(context, CartDatabase::class.java, "cart_db").build()

    @Provides
    @Singleton
    fun provideCartDao(database: CartDatabase): CartDao = database.cartDao()
}