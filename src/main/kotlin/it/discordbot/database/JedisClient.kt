package it.discordbot.database

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
class JedisClient {

	@Bean
	fun jedisConnectionFactory(): JedisConnectionFactory{
		return JedisConnectionFactory()
	}

	@Bean
	fun redisTemplate():RedisTemplate<String, Any>{
		return RedisTemplate<String, Any>().apply {
			setConnectionFactory(jedisConnectionFactory())
		}
	}

	@Bean
	fun stringRedisTemplate():StringRedisTemplate{
		return StringRedisTemplate().apply {
			setConnectionFactory(jedisConnectionFactory())
		}
	}
}