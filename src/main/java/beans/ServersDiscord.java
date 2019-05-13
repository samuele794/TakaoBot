package beans;

import org.apache.commons.text.StringEscapeUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tabella contenente tutti gli id dei canali per le funzionalità del bot
 */
@Entity
@Table(name = "`ServersDiscord`")
public class ServersDiscord {

	/**
	 * Id del server
	 */
	@Id
	@Column(name = "`SERVER_ID`")
	private String serverId;

	/**
	 * Nome del server
	 */
	@Column(name = "`NAME_SERVER`")
	private String nameServer;

	/**
	 * Simbolo di comando
	 */
	@Column(name = "`SIMBOL_COMMAND`")
	private String simbolCommand;

	/**
	 * ID Canale per invio news di BDO
	 */
	@Column(name = "`BDONewsIDChannel`")
	private String bdoNewsIDChannel;

	/**
	 * ID Canale per invio patch di BDO
	 */
	@Column(name = "`BDOPatchIDChannel`")
	private String bdoPatchIDChannel;

	/**
	 * ID Canale per invio alert boss di BDO
	 */
	@Column(name = "`BDOBossIDChannel`")
	private String bdoBossIDChannel;

	/**
	 * ID Canale per invio degli alert dell'ATM
	 */
	@Column(name = "`ATMAlertIDChannel`")
	private String atmAlertIDChannel;

	public ServersDiscord() {
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getNameServer() {
		return nameServer;
	}

	public void setNameServer(String nameServer) {
		this.nameServer = nameServer;
	}

	public String getSimbolCommand() {
		return StringEscapeUtils.unescapeJava(simbolCommand);
	}

	public void setSimbolCommand(String simbolCommand) {
		this.simbolCommand = StringEscapeUtils.escapeJava(simbolCommand);
	}

	public String getBdoNewsIDChannel() {
		return bdoNewsIDChannel;
	}

	public void setBdoNewsIDChannel(String bdoNewsIDChannel) {
		this.bdoNewsIDChannel = bdoNewsIDChannel;
	}

	public String getBdoPatchIDChannel() {
		return bdoPatchIDChannel;
	}

	public void setBdoPatchIDChannel(String bdoPatchIDChannel) {
		this.bdoPatchIDChannel = bdoPatchIDChannel;
	}

	public String getAtmAlertIDChannel() {
		return atmAlertIDChannel;
	}

	public void setAtmAlertIDChannel(String atmAlertIDChannel) {
		this.atmAlertIDChannel = atmAlertIDChannel;
	}

	public String getBdoBossIDChannel() {
		return bdoBossIDChannel;
	}

	public void setBdoBossIDChannel(String bdoBossIDChannel) {
		this.bdoBossIDChannel = bdoBossIDChannel;
	}
}
