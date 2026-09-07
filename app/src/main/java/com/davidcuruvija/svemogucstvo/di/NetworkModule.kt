package com.davidcuruvija.svemogucstvo.di

import com.davidcuruvija.svemogucstvo.BuildConfig
import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://svemogucstvo.com/"
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient : OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideWooCommerceApi(retrofit : Retrofit) : WooCommerceApi {
        return retrofit.create(WooCommerceApi::class.java)
    }
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        val credentials = Credentials.basic(
            BuildConfig.WOO_CONSUMER_KEY,
            BuildConfig.WOO_CONSUMER_SECRET
        )

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .header("Authorization", credentials)
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }
}