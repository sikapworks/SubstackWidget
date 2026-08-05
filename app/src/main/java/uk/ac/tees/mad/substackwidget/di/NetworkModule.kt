package uk.ac.tees.mad.substackwidget.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import uk.ac.tees.mad.substackwidget.data.local.WidgetPublicationsDataStore
import uk.ac.tees.mad.substackwidget.data.remote.SubstackApi
import uk.ac.tees.mad.substackwidget.data.repository.SubstackRepositoryImpl
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://substack.com/")
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSubstackApi(retrofit: Retrofit): SubstackApi =
        retrofit.create(SubstackApi::class.java)

//    @Provides
//    @Singleton
//    fun provideDataStore(@ApplicationContext context: Context): WidgetPublicationsDataStore =
//        WidgetPublicationsDataStore(context)
}