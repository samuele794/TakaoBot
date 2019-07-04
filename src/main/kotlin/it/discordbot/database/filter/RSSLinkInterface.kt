package it.discordbot.database.filter

import it.discordbot.database.repository.RSSLinkRepository
import org.springframework.context.annotation.Scope
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository

@Scope("singleton")
@Repository
class RSSLinkInterface(stringRedisTemplate: StringRedisTemplate) : RSSLinkRepository {

	private val hashOperations: ValueOperations<String, String> = stringRedisTemplate.opsForValue()

	companion object {
		private const val BDO_NEWS_KEY = "BDONewsLink"
		private const val BDO_PATCH_KEY = "BDOPatchLink"
		private const val ATM_ALERT_KEY = "ATMAlertLink"
	}

	override var lastBDONewsLink: String?
		get() = hashOperations.get(BDO_NEWS_KEY) as String
		set(value) {
			if (value != null) {
				hashOperations.set(BDO_NEWS_KEY, value)
			}
		}
	override var lastBDOPatchLink: String?
		get() = hashOperations.get(BDO_PATCH_KEY) as String
		set(value) {
			if (value != null) {
				hashOperations.set(BDO_PATCH_KEY, value)
			}
		}
	override var lastATMNewsLink: String?
		get() = hashOperations.get(ATM_ALERT_KEY) as String
		set(value) {
			if (value != null) {
				hashOperations.set(ATM_ALERT_KEY, value)
			}
		}
}