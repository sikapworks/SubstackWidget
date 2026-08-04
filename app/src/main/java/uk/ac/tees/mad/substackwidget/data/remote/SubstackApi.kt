package uk.ac.tees.mad.substackwidget.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface SubstackApi {

    @Headers("User-Agent: Mozilla/5.0 (Android)")
    @GET
    suspend fun getFeed(@Url feedUrl: String): String
}