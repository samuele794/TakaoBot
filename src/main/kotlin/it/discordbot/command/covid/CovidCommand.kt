package it.discordbot.command.covid

import com.jagrosh.jdautilities.menu.Paginator
import it.discordbot.command.checkCommand
import it.discordbot.core.JDAController
import it.discordbot.database.filter.ServerManagementInterface
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.xy.StandardXYItemRenderer
import org.jfree.data.category.DefaultCategoryDataset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.awt.BasicStroke
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


@Scope("singleton")
@Service
class CovidCommand : ListenerAdapter() {

	@Autowired
	lateinit var covidRetreiver: CovidRetreiver

	@Autowired
	lateinit var serverManagementInterface: ServerManagementInterface

	companion object {
		const val COVID_ITALY_INFO = "covidInfoIta"

		const val COVID_PROVINCE_LIST = "covidProvince"
	}

	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.author.isBot) return

		val symbolCommand = serverManagementInterface.getSimbolCommand(event.guild.id)
		when {
			checkCommand(event, symbolCommand, COVID_PROVINCE_LIST) -> {
				covidRetreiver.getProvinceList()?.let { covidList ->
					Paginator.Builder()
							.apply {
								setItemsPerPage(10)
								showPageNumbers(true)
								waitOnSinglePage(false)
								useNumberedItems(false)
								setEventWaiter(JDAController.eventWaiter)
								setTimeout(1, TimeUnit.MINUTES)
								setFinalAction {
									it.clearReactions().queue()
								}

								setUsers(event.author)
								useNumberedItems(true)

								covidList.forEach {
									addItems(it)
								}
							}.build().apply {
								display(event.channel)
							}
					return
				}
				event.channel.sendMessage("Data not Available").queue()
			}
			checkCommand(event, symbolCommand, COVID_ITALY_INFO) -> {
				val data = covidRetreiver.getCovidItalyData(region = "lombardia", province = null)

				StringBuilder().apply {
					append("Deceduti:")
					append(data.deceduti)
					appendln()
					appendln("nuovi attualmente positivi ")
					append(data.nuoviAttualmentePositivi)
					appendln("totaleAttualmentePositivi ")
					append(data.totaleAttualmentePositivi)
					appendln("Totale Casi ")
					append(data.totaleCasi)
					event.channel.sendMessage(this).queue()
				}


				val dataset = DefaultCategoryDataset()
				data.cronologiaTemporale.forEach {
					val date = LocalDateTime.ofInstant(
							Instant.ofEpochSecond(it.epocTime.toLong()), ZoneId.systemDefault()).toLocalDate().let { local ->
						StringBuilder().append(local.dayOfMonth).append("/").append(local.monthValue).append("/").append(local.year).toString()
					}
					dataset.addValue(it.deceduti.toDouble(), "deceduti", date)
					dataset.addValue(it.ricoveratiConSintomi.toDouble(), "Ricoverati con sintomi", date)
					dataset.addValue(it.dimessiGuariti.toDouble(), "Guariti", date)
				}



				ChartFactory.createLineChart("Deceduti corona",
						"Giorni",
						"Deceduti",
						dataset,
						PlotOrientation.VERTICAL,
						true,
						false,
						false
				).apply {
					val plot = this.plot
					val renderer = StandardXYItemRenderer()
					renderer.setSeriesPaint(0, Color.RED)
					renderer.setSeriesStroke(0, BasicStroke(1.0f))

					renderer.setSeriesPaint(1, Color.pink)
					renderer.setSeriesStroke(1, BasicStroke(1.0f))


//					plot.renderer = renderer
					plot.backgroundPaint = Color.lightGray
//
//					plot.setRangeGridlinesVisible(true);
//					plot.setRangeGridlinePaint(Color.BLACK);
//
//					plot.setDomainGridlinesVisible(true);
//					plot.setDomainGridlinePaint(Color.BLACK);

//					this.legend.frame = BlockBorder.NONE;

					val out = ByteArrayOutputStream()

					ChartUtils.writeChartAsPNG(out, this, 1920, 1080)
					event.channel.sendFile(out.toByteArray(), "gr.jpg").queue()
				}

			}
		}
	}
}