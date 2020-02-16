package it.discordbot.database

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory


@Configuration
class RedisLattugaConfig {

	@Value("\${spring.redis.host}")
	lateinit var hostName: String

	@Value("\${spring.redis.port}")
	var serverPort: Int = 0

	@Bean
	fun redisConnectionFactory(): LettuceConnectionFactory {
		return LettuceConnectionFactory(RedisStandaloneConfiguration(hostName, serverPort))
	}
}