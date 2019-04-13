package interfaces;

import beans.ServerToChannel;
import beans.ServersDiscord;
import org.apache.commons.text.StringEscapeUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import starter.Start;

import javax.persistence.TypedQuery;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class PostgreSQLInterface {

	private static SessionFactory sessionFactory;

	/**
	 * Metodo per inizializzare la connessione
	 */
	public static void initializeDB() {
		String fileName = "hibernate.cfg.xml";

		File file = new File("resources/" + fileName);

		if (!file.exists()) {
			//risorsa su IDE
			file = new File(PostgreSQLInterface.class.getClassLoader().getResource(fileName).getFile());
		}

		sessionFactory = new Configuration().configure(file).buildSessionFactory();


	}

	/**
	 * Metodo che aggiunge nome e l'id del server che ha aggiunto il bot sul database.
	 *
	 * @param nameServer Nome del server
	 * @param serverID   ID del server Discord
	 */
	public static void newServer(String nameServer, String serverID) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		ServersDiscord serversDiscord = new ServersDiscord();
		serversDiscord.setServerId(serverID);
		serversDiscord.setNameServer(nameServer);
		serversDiscord.setSimbolCommand("%");


		session.save(serversDiscord);
		transaction.commit();
		session.close();

	}

	/**
	 * Metodo che rimuove la riga del server che ha espulso il bot
	 *
	 * @param serverID id del server
	 */
	public static void deleteServer(String serverID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		ServersDiscord serverDiscord = new ServersDiscord();
		serverDiscord.setServerId(serverID);

		session.delete(serverDiscord);

		transaction.commit();
		session.close();
	}

	public static void updateNameServer(String serverID, String name) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE ServersDiscord set nameServer = :nameServer where serverId = :serverID");
		query.setParameter("serverID", serverID);
		query.setParameter("nameServer", name);

		query.executeUpdate();

		transaction.commit();
		session.close();
	}

	/**
	 * Metodo che per ottenre il simbolo del server
	 *
	 * @param serverID id del server
	 * @return prefisso per comandi
	 */
	public static String getSimbol(String serverID) {

		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select simbolCommand from ServersDiscord where serverId = :serverId");
		query.setParameter("serverId", serverID);
		String simbol = (String) query.uniqueResult();
		session.close();

		return simbol;
	}

	/**
	 * Metodo per configurare il simbolo di comando per il server
	 *
	 * @param command  Simbolo del comando nuovo
	 * @param serverID ID del server
	 */
	public static void setSimbol(String command, String serverID) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("UPDATE ServersDiscord SET simbolCommand = :simbol WHERE serverId = :serverId");

		query.setParameter("simbol", StringEscapeUtils.escapeJava(command));
		query.setParameter("serverId", serverID);
		query.executeUpdate();

		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per settare il canale per le news su BDO
	 *
	 * @param serverID  id del server
	 * @param channelID id del canale testuale
	 */
	public static void setBDONewsChannel(String serverID, String channelID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE ServersDiscord set bdoNewsIDChannel = :channelId where serverId = :serverId");

		query.setParameter("channelId", channelID);
		query.setParameter("serverId", serverID);
		query.executeUpdate();

		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per rimuovere il canale per le news su BDO
	 *
	 * @param serverID id del server
	 */
	public static void removeBDONewsChannel(String serverID) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE ServersDiscord SET bdoNewsIDChannel = NULL WHERE serverId = :serverId");

		query.setParameter("serverId", serverID);
		query.executeUpdate();

		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per ottenere tutti i canali registrati alle news di BDO
	 *
	 * @return Lista di coppie di serverID e channelID
	 * @see ServerToChannel ServerToChannel
	 */

	public static ArrayList<ServerToChannel> getBDONewsChannel() {

		Session session = sessionFactory.openSession();

		TypedQuery<ServerToChannel> q = session.createQuery("SELECT new beans.ServerToChannel(serverId, bdoNewsIDChannel) from ServersDiscord where bdoNewsIDChannel is not null ", ServerToChannel.class);
		ArrayList<ServerToChannel> serverToChannels = (ArrayList<ServerToChannel>) q.getResultList();
		session.close();

		return serverToChannels;
	}

	/**
	 * Metodo per settare il canale per le patch di BDO
	 *
	 * @param serverID  id del server
	 * @param channelID id del canale testuale
	 */
	public static void setBDOPatchChannel(String serverID, String channelID) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("UPDATE ServersDiscord SET bdoPatchIDChannel = :channelId WHERE serverId = :serverId");
		query.setParameter("serverId", serverID);
		query.setParameter("channelId", channelID);

		query.executeUpdate();

		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per rimuovere il canale per le patch di BDO
	 *
	 * @param serverID id del server
	 */
	public static void removeBDOPatchChannel(String serverID) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("UPDATE ServersDiscord SET bdoPatchIDChannel = NULL WHERE serverId = :serverId");
		query.setParameter("serverId", serverID);

		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per settare il canale per i boss alert di BDO
	 *
	 * @param serverID  id del server
	 * @param channelID id del canale testuale
	 */

	public static void setBDOBossChannel(String serverID, String channelID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();


		Query query = session.createQuery("UPDATE ServersDiscord set bdoBossIDChannel = :channelID WHERE serverId = :serverId");
		query.setParameter("serverId", serverID);
		query.setParameter("channelID", channelID);

		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per rimuovere il canale per  i boss alert di BDO
	 *
	 * @param serverID id del server
	 */
	public static void removeBDOBossChannel(String serverID) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE ServersDiscord SET bdoBossIDChannel = NULL where serverId = :serverId");
		query.setParameter("serverId", serverID);

		query.executeUpdate();

		transaction.commit();
		session.close();
	}


	/**
	 * Metodo per ottenere tutti i canali registrati agli alert dei boss di BDO
	 *
	 * @return Lista di coppie di serverID e channelID
	 * @see ServerToChannel ServerToChannel
	 */

	public static ArrayList<ServerToChannel> getBDOBossChannel() {

		Session session = sessionFactory.openSession();

		TypedQuery<ServerToChannel> query = session.createQuery("SELECT new beans.ServerToChannel(serverId, bdoBossIDChannel) from ServersDiscord where bdoBossIDChannel is not null ", ServerToChannel.class);

		ArrayList<ServerToChannel> list = (ArrayList<ServerToChannel>) query.getResultList();

		session.close();
		return list;
	}

	/**
	 * Metodo per ottenere tutti i canali registrati alle patch di BDO
	 *
	 * @return lista dei server e dei relativi canali che si sono registrati
	 * @see ServerToChannel ServerToChannel
	 */
	public static ArrayList<ServerToChannel> getBDOPatchChannel() {

		Session session = sessionFactory.openSession();

		TypedQuery<ServerToChannel> query = session.createQuery("SELECT new beans.ServerToChannel(serverId, bdoPatchIDChannel) from ServersDiscord where bdoPatchIDChannel is not null ", ServerToChannel.class);
		ArrayList<ServerToChannel> list = (ArrayList<ServerToChannel>) query.getResultList();

		return list;
	}

	/**
	 * Ottiene la lista delle ultime notizie di BDO
	 *
	 * @return ArrayList lista delle ultime notizie
	 */
	public static ArrayList getListNewsBDO() {

		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select lastNewsBDO from Rsslink where id = 1");

		ArrayList list = Start.gson.fromJson((String) query.uniqueResult(), ArrayList.class);
		session.close();

		return list;
	}

	/**
	 * Imposta la lista delle ultime notizie di bdo
	 *
	 * @param newsBDOList Lista delle ultime notizie di BDO
	 */
	public static void setNewsBDO(ArrayList<String> newsBDOList) {

		if (newsBDOList.size() > 15) {
			Iterator<String> iteratorList = newsBDOList.iterator();
			int cont = 0;
			while (iteratorList.hasNext()) {
				iteratorList.next();
				iteratorList.remove();
				if (cont == 7) {
					break;
				}
				cont++;
			}
		}
		String json = Start.gson.toJson(newsBDOList);

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE Rsslink set lastNewsBDO = :json where id = 1");
		query.setParameter("json", json);

		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	/**
	 * Ottiene il link dell'ultima patch di BDO
	 *
	 * @return String link url dell'ultima patch
	 */
	public static String getLastPatchBDO() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("SELECT lastPatchBDO from Rsslink WHERE id = 1");

		String lastPatch = (String) query.uniqueResult();
		session.close();

		return lastPatch;
	}

	/**
	 * Imposta l'ultima patch di BDO, se esiste l'aggiorna, altrimenti inserisce la riga.
	 *
	 * @param url url dell'ultimo feed rss aggiornato
	 */
	public static void setLastPatchBDO(String url) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE Rsslink set lastPatchBDO = :url where id = 1");

		query.setParameter("url", url);
		query.executeUpdate();

		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per settare il canale per gli alert dell'ATM
	 *
	 * @param serverID  id del server
	 * @param channelID id del canale testuale
	 */
	public static void setATMAlertChannel(String serverID, String channelID) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE ServersDiscord set atmAlertIDChannel = :channelID where serverId = :serverID");
		query.setParameter("channelID", channelID);
		query.setParameter("serverID", serverID);

		query.executeUpdate();

		transaction.commit();
		session.close();
	}

	/**
	 * Metodo per ottenere tutti i canali registrati agli alert di ATM
	 *
	 * @return lista dei server e dei relativi canali che si sono registrati
	 * @see ServerToChannel ServerToChannel
	 */
	public static ArrayList<ServerToChannel> getATMAlertChannel() {

		Session session = sessionFactory.openSession();

		TypedQuery<ServerToChannel> query = session.createQuery("SELECT new beans.ServerToChannel(serverId, atmAlertIDChannel) from ServersDiscord where atmAlertIDChannel is not null ", ServerToChannel.class);
		ArrayList<ServerToChannel> list = (ArrayList<ServerToChannel>) query.getResultList();

		session.close();
		return list;
	}

	/**
	 * Metodo per rimuovere il canale per gli alert di ATM
	 *
	 * @param serverID id del server
	 */
	public static void removeATMAlertChannel(String serverID) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE ServersDiscord set atmAlertIDChannel = null where serverId = :serverID");
		query.setParameter("serverID", serverID);

		query.executeUpdate();
		transaction.commit();
		session.close();
	}

	/**
	 * Ottiene il link dell'ultimo alert di atm
	 *
	 * @return String link url dell'ultimo alert di atm
	 */
	public static String getLastATMAlert() {

		Session session = sessionFactory.openSession();

		Query query = session.createQuery("SELECT lastAtmAlert from Rsslink where id= 1");
		String url = (String) query.uniqueResult();

		session.close();
		return url;
	}

	/**
	 * Imposta l'ultima patch di BDO, se esiste l'aggiorna, altrimenti inserisce la riga.
	 *
	 * @param url url dell'ultimo feed rss aggiornato
	 */
	public static void setLastATMAlert(String url) {

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE Rsslink set lastAtmAlert = :url where id = 1");
		query.setParameter("url", url);
		query.executeUpdate();

		transaction.commit();
		session.close();
	}


}
