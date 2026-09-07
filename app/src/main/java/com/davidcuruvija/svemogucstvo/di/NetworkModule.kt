package com.davidcuruvija.svemogucstvo.di

import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://svemogucstvo.com/"
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideWooCommerceApi(retrofit : Retrofit) : WooCommerceApi {
        return retrofit.create(WooCommerceApi::class.java)
    }
}