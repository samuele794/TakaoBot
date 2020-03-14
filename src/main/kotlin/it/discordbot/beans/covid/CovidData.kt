package it.discordbot.beans.covid

import com.fasterxml.jackson.annotation.JsonProperty

class CovidData {
	@JsonProperty("codice_regione")
	val codiceRegione: Int = 0

	@JsonProperty("deceduti")
	val deceduti: Int = 0

	@JsonProperty("denominazione_regione")
	lateinit var denominazioneRegione: String

	@JsonProperty("dimessi_guariti")
	val dimessiGuariti: Int = 0

	@JsonProperty("isolamento_domiciliare")
	val isolamentoDomiciliare: Int = 0

	val lat: Double = 0.0

	val long: Double = 0.0

	@JsonProperty("nuovi_attualmente_positivi")
	val nuoviAttualmentePositivi: Int = 0

	@JsonProperty("province")
	lateinit var province: List<Province>

	@JsonProperty("ricoverati_con_sintomi")
	val ricoveratiConSintomi: Int = 0

	@JsonProperty("tamponi")
	val tamponi: Int = 0

	@JsonProperty("terapia_intensiva")
	val terapiaIntensiva: Int = 0

	@JsonProperty("totale_attualmente_positivi")
	val totaleAttualmentePositivi: Int = 0

	@JsonProperty("totale_casi")
	val totaleCasi: Int = 0

	@JsonProperty("totale_ospedalizzati")
	val totaleOspedalizzati: Int = 0

	@JsonProperty("ts")
	lateinit var cronologiaTemporale: List<TimeSerie>
}