package com.ramesh.roundupsavingapp.di

import android.app.Application
import android.content.Context
import com.ramesh.roundupsavingapp.data.network.ApiService
import com.ramesh.roundupsavingapp.data.repository.SavingsRepositoryImpl
import com.ramesh.roundupsavingapp.domain.repository.SavingsRepository
import com.ramesh.roundupsavingapp.domain.usecase.CreateSavingsGoalUseCase
import com.ramesh.roundupsavingapp.domain.usecase.RoundUpAndSaveUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSavingsRepository(apiService: ApiService): SavingsRepository =
        SavingsRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideRoundUpAndSaveUseCase(repository: SavingsRepository): RoundUpAndSaveUseCase =
        RoundUpAndSaveUseCase(repository)


    @Provides
    @Singleton
    fun provideCreateSavingsGoalUseCase(repository: SavingsRepository): CreateSavingsGoalUseCase {
        return CreateSavingsGoalUseCase(repository)
    }

}