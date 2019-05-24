package it.discordbot.command.tpl.atm.girocitta.status

import it.discordbot.beans.atm.LineaMetroATM
import it.discordbot.beans.atm.MetroDirezioneStatus
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Scope("singleton")
@Component
class ATMMetroStatus {
	//regolare verde
	//rallentata giallo
	//interrotta rosso

	private val nomiLineeMetro = arrayOf("RHO FIERAMILANO",
			"BISCEGLIE",
			"SESTO F.S.",
			"GESSATE",
			"COLOGNO NORD",
			"ABBIATEGRASSO",
			"ASSAGO FORUM",
			"COMASINA",
			"SAN DONATO",
			"BIGNAMI",
			"SAN SIRO STADIO")

	private val okHttpClient = OkHttpClient()

	fun getStatoMetro(event: MessageReceivedEvent) {
		val request = Request.Builder().apply {
			url("https://www.atm.it/it/Pagine/default.aspx")
		}.build()

		val response = okHttpClient.newCall(request).execute()

		val doc = Jsoup.parse(response.body()!!.string())
		val listElementClass = doc.getElementById("StatusLinee")
				.getElementById("StatusLinee").getElementsByClass("StatusLinee_Stretch")

		val message = prepareMessage(fillStatusMetro(listElementClass))

		event.textChannel.sendMessage(message).queue()

	}


	fun prepareMessage(listaStatusLinee: ArrayList<LineaMetroATM>): Message? {
		val format = "%1\$-20s %2\$5s \n"

		val messageString = StringBuilder()

		for (el in listaStatusLinee) {
			messageString.append(el.nomeLineMetro).append("\n\n")
			for (direzione in el.metroDirezioneStatusList) {
				messageString.apply {
					var statoString = String()

					when (direzione.stato) {
						"Regolare" -> {
							statoString = "[ " + direzione.stato + " ]()"
						}
						"Rallentata" -> {
							statoString = "< " + direzione.stato + " >"
						}
						"Interrotta" -> {
							statoString = "[]( " + direzione.stato + " )"
						}
					}
					append(String.format(format, direzione.nomeDirezione, statoString))
				}
			}
			messageString.append("\n--------------------------------------------------\n\n")
		}

		return MessageBuilder().apply {
			appendCodeBlock(messageString.toString(), "mkd")
		}.build()
	}


	private fun fillStatusMetro(listStatusLinee: Elements): ArrayList<LineaMetroATM> {
		val metroList = getListMetro()
		var cont = 1
		for (n in 0..3) {
			metroList[n].metroDirezioneStatusList.forEach {
				it.stato = listStatusLinee[cont].text()
				cont++
			}
		}

		return metroList

	}

	private fun getListMetro(): ArrayList<LineaMetroATM> {
		val diM1 = ArrayList<MetroDirezioneStatus>()
		val diM2 = ArrayList<MetroDirezioneStatus>()
		val diM3 = ArrayList<MetroDirezioneStatus>()
		val diM5 = ArrayList<MetroDirezioneStatus>()
		for (n in 0..10) {
			when (n) {
				0, 1, 2 -> {
					MetroDirezioneStatus(nomiLineeMetro[n], "").let {
						diM1.add(it)
					}
				}
				3, 4, 5, 6 -> {
					MetroDirezioneStatus(nomiLineeMetro[n], "").let {
						diM2.add(it)
					}
				}

				7, 8 -> {
					MetroDirezioneStatus(nomiLineeMetro[n], "").let {
						diM3.add(it)
					}
				}
				9, 10 -> {
					MetroDirezioneStatus(nomiLineeMetro[n], "").let {
						diM5.add(it)
					}
				}
			}
		}

		return ArrayList<LineaMetroATM>().apply {
			add(LineaMetroATM("M1", diM1))
			add(LineaMetroATM("M2", diM2))
			add(LineaMetroATM("M3", diM3))
			add(LineaMetroATM("M5", diM5))
		}
	}
}