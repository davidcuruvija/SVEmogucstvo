package com.davidcuruvija.svemogucstvo.di

import com.davidcuruvija.svemogucstvo.BuildConfig
import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceApi
import com.davidcuruvija.svemogucstvo.data.remote.WooCommerceStoreApi
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
    fun provideWooCommerceStoreApi(
        retrofit : Retrofit
    ) : WooCommerceStoreApi {
        return retrofit.create(WooCommerceStoreApi::class.java)
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
                val builder = request.newBuilder()
                if (!request.url.encodedPath.contains("wp-json/wc/store")) {
                    builder.header("Authorization", credentials)
                }

                builder.header("User-Agent", "SveMogucstvo-AndroidApp")

                chain.proceed(builder.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }
}