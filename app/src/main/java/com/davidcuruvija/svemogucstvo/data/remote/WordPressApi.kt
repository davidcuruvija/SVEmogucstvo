package com.davidcuruvija.svemogucstvo.data.remote

import com.davidcuruvija.svemogucstvo.model.page.PageDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WordPressApi {
    @GET("wp-json/wp/v2/pages/{id}")
    suspend fun getPage(@Path("id") id : Int) : PageDto
}
