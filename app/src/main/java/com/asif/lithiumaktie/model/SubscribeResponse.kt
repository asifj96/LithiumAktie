package com.asif.lithiumaktie.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SubscribeResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)