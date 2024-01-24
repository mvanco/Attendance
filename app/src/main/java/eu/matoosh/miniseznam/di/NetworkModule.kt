package eu.matoosh.miniseznam.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.matoosh.miniseznam.api.SeznamService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideSeznamService(): SeznamService {
        return SeznamService.create()
    }
}