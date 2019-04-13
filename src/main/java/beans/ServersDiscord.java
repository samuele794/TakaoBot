package beans;

import org.apache.commons.text.StringEscapeUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "`ServersDiscord`")
public class ServersDiscord {

	@Id
	@Column(name = "`SERVER_ID`")
	private String serverId;
	@Column(name = "`NAME_SERVER`")
	private String nameServer;
	@Column(name = "`SIMBOL_COMMAND`")
	private String simbolCommand;
	@Column(name = "`BDONewsIDChannel`")
	private String bdoNewsIDChannel;
	@Column(name = "`BDOPatchIDChannel`")
	private String bdoPatchIDChannel;
	@Column(name = "`BDOBossIDChannel`")
	private String bdoBossIDChannel;
	@Column(name = "`ATMAlertIDChannel`")
	private String atmAlertIDChannel;

	public ServersDiscord() {
	}

	public ServersDiscord(String serverId, String bdoNewsIDChannel) {
		this.serverId = serverId;
		this.bdoNewsIDChannel = bdoNewsIDChannel;
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
