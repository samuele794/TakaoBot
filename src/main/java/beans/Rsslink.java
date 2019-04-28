package beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tabella RSSLink
 */
@Entity
@Table(name = "`RSSLink`")
public class Rsslink {

	@Id
	@Column(name = "`id`")
	private long id;
	/**
	 * Lista link ultime news di BDO
	 */
	@Column(name = "`LastNewsBDO`")
	private String lastNewsBDO;
	@Column(name = "`LastPatchBDO`")
	/**
	 * Link ultima patch di BDO
	 */
	private String lastPatchBDO;
	/**
	 * Link ultimo feed di atm
	 */
	@Column(name = "`LastATMAlert`")
	private String lastAtmAlert;

	public Rsslink() {
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getLastNewsBDO() {
		return lastNewsBDO;
	}

	public void setLastNewsBDO(String lastNewsBDO) {
		this.lastNewsBDO = lastNewsBDO;
	}


	public String getLastPatchBDO() {
		return lastPatchBDO;
	}

	public void setLastPatchBDO(String lastPatchBDO) {
		this.lastPatchBDO = lastPatchBDO;
	}


	public String getLastAtmAlert() {
		return lastAtmAlert;
	}

	public void setLastAtmAlert(String lastAtmAlert) {
		this.lastAtmAlert = lastAtmAlert;
	}

}
