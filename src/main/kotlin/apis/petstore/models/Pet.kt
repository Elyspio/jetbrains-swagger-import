/**
 * Swagger Petstore
 *
 * This is a sample server Petstore server.  You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, you can use the api key `special-key` to test the authorization filters.
 *
 * The version of the OpenAPI document: 1.0.5
 * Contact: apiteam@swagger.io
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package apis.petstore.models

import apis.petstore.models.Category
import apis.petstore.models.Tag

import com.google.gson.annotations.SerializedName

/**
 * 
 *
 * @param name 
 * @param photoUrls 
 * @param id 
 * @param category 
 * @param tags 
 * @param status pet status in the store
 */

data class Pet (

    @SerializedName("name")
    val name: kotlin.String,

    @SerializedName("photoUrls")
    val photoUrls: kotlin.collections.List<kotlin.String>,

    @SerializedName("id")
    val id: kotlin.Long? = null,

    @SerializedName("category")
    val category: Category? = null,

    @SerializedName("tags")
    val tags: kotlin.collections.List<Tag>? = null,

    /* pet status in the store */
    @SerializedName("status")
    val status: Pet.Status? = null

) {

    /**
     * pet status in the store
     *
     * Values: available,pending,sold
     */
    enum class Status(val value: kotlin.String) {
        @SerializedName(value = "available") available("available"),
        @SerializedName(value = "pending") pending("pending"),
        @SerializedName(value = "sold") sold("sold");
    }
}