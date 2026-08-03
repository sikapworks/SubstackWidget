package uk.ac.tees.mad.substackwidget.di

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import uk.ac.tees.mad.substackwidget.data.local.WidgetPublicationsDataStore
import uk.ac.tees.mad.substackwidget.data.remote.SubstackApi
import uk.ac.tees.mad.substackwidget.data.repository.SubstackRepositoryImpl
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository
import uk.ac.tees.mad.substackwidget.domain.usecase.GetGroupedFeedUseCase
import uk.ac.tees.mad.substackwidget.domain.usecase.ManagePublicationsUseCase
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://substack.com/") // placeholder; @Url overrides it per-call
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val api = retrofit.create(SubstackApi::class.java)
    private val localDataSource = WidgetPublicationsDataStore(context)

    val repository: SubstackRepository = SubstackRepositoryImpl(api, localDataSource)

    val getGroupedFeedUseCase = GetGroupedFeedUseCase(repository)
    val managePublicationsUseCase = ManagePublicationsUseCase(repository)
}