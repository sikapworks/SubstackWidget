package uk.ac.tees.mad.substackwidget.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface SubstackApi {

    @GET
    suspend fun getFeed(@Url feedUrl: String): String
}