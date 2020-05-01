package com.example.woltexercise.data

import com.google.gson.annotations.SerializedName

data class Venues (
    @SerializedName("results") val results : List<Results>,
    @SerializedName("status") val status : String
)

data class Results (
    @SerializedName("address") val address : String,
    @SerializedName("city") val city : String,
    @SerializedName("country") val country : String,
    @SerializedName("currency") val currency : String,
    @SerializedName("description") val description : List<Description>,
    @SerializedName("short_description") val shortDescription : List<ShortDescription>,
    @SerializedName("favourite") var favourite : Boolean,
    @SerializedName("id") val id : Id,
    @SerializedName("listimage") val listImage : String,
    @SerializedName("name") val name : List<Name>
)

data class Id (
    @SerializedName("\$oid") val oid : String
)

data class Description (
    @SerializedName("lang") val lang : String,
    @SerializedName("value") val value : String
)

data class Name (
    @SerializedName("lang") val lang : String,
    @SerializedName("value") val value : String
)

data class ShortDescription (
    @SerializedName("lang") val lang : String,
    @SerializedName("value") val value : String
)
