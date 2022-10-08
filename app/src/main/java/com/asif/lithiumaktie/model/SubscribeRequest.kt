package com.asif.lithiumaktie.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SubscribeRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("is_accept")
    val is_accept: String
)