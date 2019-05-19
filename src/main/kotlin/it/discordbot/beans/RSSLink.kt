package it.discordbot.beans

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Entità che rappresenta la tabella contenitore dei link RSS
 * @property id Long id riga
 * @property lastNewsBDO String? link dell'ultima news di BDO
 * @property lastPatchBDO String? link dell'ultima patch di BDO
 * @property lastAtmAlert String? link dell'ultima news di ATM
 */
@Entity
@Table(name = "`RSSLink`")
class RSSLink {
	@Id
	@Column(name = "`id`")
	var id: Long = 0
	/**
	 * Lista link ultime news di BDO
	 */
	@Column(name = "`LastNewsBDO`")
	var lastNewsBDO: String? = null

	/**
	 * Link ultima patch di BDO
	 */
	@Column(name = "`LastPatchBDO`")
	var lastPatchBDO: String? = null
	/**
	 * Link ultimo feed di atm
	 */
	@Column(name = "`LastATMAlert`")
	var lastAtmAlert: String? = null
}