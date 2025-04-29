package com.example.alquranapp.api
import com.example.alquranapp.SurahDetailsResponse
import com.example.alquranapp.SurahListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApi {
    @GET("surah")
    fun getSurahList(): Call<SurahListResponse>

    @GET("surah/{id}/{edition}")
    suspend fun getSurahDetails(
        @Path("id") id: Int,
        @Path("edition") edition: String
    ): SurahDetailsResponse
}
