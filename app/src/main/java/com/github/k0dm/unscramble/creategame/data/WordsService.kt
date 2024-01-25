package com.github.k0dm.unscramble.creategame.data

import retrofit2.http.GET
import retrofit2.http.Query

interface WordsService {

    @GET("word")
    suspend fun words(@Query("number") number: Int): List<String>
}