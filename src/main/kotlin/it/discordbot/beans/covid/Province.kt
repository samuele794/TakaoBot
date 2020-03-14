package it.discordbot.beans.covid

import com.fasterxml.jackson.annotation.JsonProperty


class Province {
	@JsonProperty("codice_provincia")
	val codiceProvincia: Int = 0

	@JsonProperty("denominazione_provincia")
	lateinit var denominazioneProvincia: String

	val lat: Double = 0.0

	@JsonProperty("long")
	val lng: Double = 0.0

	@JsonProperty("sigla_provincia")
	lateinit var siglaProvincia: String

	@JsonProperty("totale_casi")
	val totaleCasi: Int = 0
}