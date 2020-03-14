package it.discordbot.command.covid.services

import it.discordbot.beans.covid.CovidData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface CovidService {

	@GET("italy/{region}/{province}/data.json")
	fun getCovidItalyDataRegionProvince(@Path("region") region: String, @Path("province") province: String?): Call<CovidData>


	@GET("italy/{region}/data.json")
	fun getCovidItalyDataRegion(@Path("region") region: String): Call<CovidData>

}