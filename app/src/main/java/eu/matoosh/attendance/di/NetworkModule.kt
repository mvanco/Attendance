package eu.matoosh.attendance.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.matoosh.attendance.api.IceAppService
import eu.matoosh.attendance.seznam.api.SeznamService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideIceAppService(): IceAppService {
        return IceAppService.create()
    }

    @Singleton
    @Provides
    fun provideSeznamService(): SeznamService {
        return SeznamService.create()
    }
}