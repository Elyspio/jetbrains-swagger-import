package example

import apis.petstore.apis.PetApi
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun main() {
    // Configure retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://petstore.swagger.io/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create your service
    val service: PetApi = retrofit.create(PetApi::class.java)

    // Execute the request
    val response = service.getPetById(1).execute()

    // Do whatever you want with the response
    val gson = GsonBuilder().setPrettyPrinting().create()
    print(gson.toJson(response.body()))

}