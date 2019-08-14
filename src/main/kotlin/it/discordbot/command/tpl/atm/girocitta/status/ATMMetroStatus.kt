package it.discordbot.command.tpl.atm.girocitta.status

import it.discordbot.beans.atm.LineaMetroATM
import it.discordbot.beans.atm.MetroDirezioneStatus
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Classe per leggere e interpretare lo stato della metro ATm
 * @property nomiLineeMetro Array<String> lista della direzioni delle metro ATM
 * @property okHttpClient OkHttpClient
 */
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

	private val lineeMetro = arrayOf("M1", "M2", "M3", "M4", "M5")

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


	fun prepareMessage(listaStatusLinee: ArrayList<LineaMetroATM>): Message {
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
							statoString = "[_]( " + direzione.stato + " )"
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
			add(LineaMetroATM(lineeMetro[0], diM1))
			add(LineaMetroATM(lineeMetro[1], diM2))
			add(LineaMetroATM(lineeMetro[2], diM3))
//			add(LineaMetroATM(lineeMetro[3], diM4))
			add(LineaMetroATM(lineeMetro[4], diM5))
		}
	}
}