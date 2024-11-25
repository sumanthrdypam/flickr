package com.sam.flickr.data.api

import com.sam.flickr.data.api.Constant.END_POINT
import com.sam.flickr.data.dto.ImageEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(END_POINT)
    suspend fun getImages(
        @Query("format") format :String = "json",
        @Query("nojsoncallback") noJsonCallback : Int = 1,
        @Query("tags") tags:String
    ) : Response<ImageEntity>
}