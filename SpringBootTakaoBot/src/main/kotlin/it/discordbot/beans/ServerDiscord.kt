package it.discordbot.beans

import org.apache.commons.text.StringEscapeUtils
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "`ServersDiscord`")
class ServerDiscord {
	/**
	 * Id del server
	 */
	@Id
	@Column(name = "`SERVER_ID`")
	var serverId: String? = null

	/**
	 * Nome del server
	 */
	@Column(name = "`NAME_SERVER`")
	var nameServer: String? = null

	/**
	 * Simbolo di comando
	 */

	@Column(name = "`SIMBOL_COMMAND`")
	var simbolCommand: String? = ""
		get() = StringEscapeUtils.unescapeJava(field)
		set(value) {
			field = StringEscapeUtils.escapeJava(value)
		}


	/**
	 * ID Canale per invio news di BDO
	 */
	@Column(name = "`BDONewsIDChannel`")
	var bdoNewsIDChannel: String? = null

	/**
	 * ID Canale per invio patch di BDO
	 */
	@Column(name = "`BDOPatchIDChannel`")
	var bdoPatchIDChannel: String? = null

	/**
	 * ID Canale per invio alert boss di BDO
	 */
	@Column(name = "`BDOBossIDChannel`")
	var bdoBossIDChannel: String? = null

	/**
	 * ID Canale per invio degli alert dell'ATM
	 */
	@Column(name = "`ATMAlertIDChannel`")
	var atmAlertIDChannel: String? = null

}