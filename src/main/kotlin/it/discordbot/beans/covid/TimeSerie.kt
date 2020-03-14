package it.discordbot.beans.covid

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

@JsonRootName("T")
class TimeSerie {

	@JsonProperty("deceduti")
	val deceduti: Int = 0

	@JsonProperty("dimessi_guariti")
	val dimessiGuariti: Int = 0

	@JsonProperty("isolamento_domiciliare")
	val isolamentoDomiciliare: Int = 0

	@JsonProperty("nuovi_attualmente_positivi")
	val nuoviAttualmentePositivi: Int = 0

	@JsonProperty("ricoverati_con_sintomi")
	val ricoveratiConSintomi: Int = 0

	@JsonProperty("t")
	val epocTime: Long = 0

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

}