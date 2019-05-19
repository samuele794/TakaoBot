package it.discordbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class StartBot

fun main(args: Array<String>) {
	runApplication<StartBot>(*args)
}
