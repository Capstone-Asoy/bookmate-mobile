package com.example.bookmate.data.retrofit

import com.example.bookmate.data.request.LoginRequest
import com.example.bookmate.data.request.PreferenceRequest
import com.example.bookmate.data.response.GenreResponse
import com.example.bookmate.data.response.LoginResponse
import com.example.bookmate.data.response.PreferenceResponse
import com.example.bookmate.data.response.ProfileResponse
import com.example.bookmate.data.response.RecommendationResponse
import com.example.bookmate.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("register")
    fun register(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part?,
    ): Call<RegisterResponse>

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @GET("me")
    fun getProfile(): Call<ProfileResponse>

    @GET("genres")
    fun getGenres(): Call<GenreResponse>

    @POST("preference")
    fun submitPreferences(@Body selectedGenres: PreferenceRequest): Call<PreferenceResponse>

    @GET("getRekomendasi")
    fun getRekomendasi(): Call<RecommendationResponse>

}