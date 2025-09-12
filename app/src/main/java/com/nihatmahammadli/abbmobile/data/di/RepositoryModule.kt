package com.nihatmahammadli.abbmobile.data.di

import com.nihatmahammadli.abbmobile.data.repository.CardRepositoryImpl
import com.nihatmahammadli.abbmobile.domain.repository.CardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        impl: CardRepositoryImpl
    ): CardRepository
}