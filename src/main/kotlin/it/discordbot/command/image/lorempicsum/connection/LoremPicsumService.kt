package it.discordbot.command.image.lorempicsum.connection

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 * Realmente viene passato i dati dell'Immagine, ma a me basta l'url
 */
interface LoremPicsumService {

    @GET("{width}/{height}")
    fun getImageRandom(@Path("width") width: Int, @Path("height") height: Int): Call<String>

    @GET("{width}/{height}?grayscale")
    fun getImageRandomGrayScale(@Path("width") width: Int, @Path("height") height: Int): Call<String>

    @GET("{width}/{height}?grayscale")
    fun getImageRandomGrayScaleBlur(@Path("width") width: Int, @Path("height") height: Int, @Query("blur") blurScale: Int): Call<String>

    @GET("{width}/{height}")
    fun getImageRandomBlur(@Path("width") width: Int, @Path("height") height: Int, @Query("blur") blurScale: Int): Call<String>

}