package com.balazs.otpflickertask.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.balazs.otpflickertask.data.local.OtpSharedPreferencesStorage
import com.balazs.otpflickertask.data.local.userDataStore
import com.balazs.otpflickertask.data.network.FlickerHttpClientFactory
import com.balazs.otpflickertask.data.remote.FlickerApi
import com.balazs.otpflickertask.data.repository.FlickrPictureRepository
import com.balazs.otpflickertask.domain.repository.PictureRepository
import com.balazs.otpflickertask.domain.repository.SearchTermRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    @Singleton
    fun provideFlickerHttpClient(): HttpClient {
        return FlickerHttpClientFactory().build()
    }

    @Provides
    @Singleton
    fun provideFlickerApi(
        httpClient: HttpClient,
    ): FlickerApi {
        return FlickerApi(
            httpClient = httpClient,
        )
    }

    @Provides
    @Singleton
    fun provideDataStoreSharedPreferences(
        @ApplicationContext appContext: Context,
    ): DataStore<Preferences> {
        return appContext.userDataStore
    }

    @Provides
    @Singleton
    fun provideOtpSharedPreferencesStorage(
        dataSore: DataStore<Preferences>
    ): OtpSharedPreferencesStorage {
        return OtpSharedPreferencesStorage(
            dataSore = dataSore
        )
    }

    @Provides
    @Singleton
    fun provideSearchTermRepository(
        otpSharedPreferencesStorage: OtpSharedPreferencesStorage,
    ): SearchTermRepository {
        return com.balazs.otpflickertask.data.repository.SearchTermRepository(
            otpSharedPreferencesStorage = otpSharedPreferencesStorage
        )
    }

    @Provides
    @Singleton
    fun providePictureRepository(
        flickerApi: FlickerApi,
    ): PictureRepository {
        return FlickrPictureRepository(
            flickerApi = flickerApi,
        )
    }
}