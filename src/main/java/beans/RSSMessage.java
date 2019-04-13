package beans;

import org.jsoup.nodes.Document;

/**
 * Classe contenitore per il titolo, il link e il body HTML
 * del feed RSS
 */

public class RSSMessage {
    private String title;
    private String link;
    private Document doc;


    public RSSMessage(String title, String link, Document doc){
        setTitle(title);
        setLink(link);
        setDoc(doc);
    }

    /**
     * Metodo per ottenre il titolo del feed
     *
     * @return String titolo del feed
     */
    public String getTitle() {
        return title;
    }

    /**
     * Metodo per ottenre il titolo del feed
     *
     * @param title titolo del feed
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Metodo per ottenere il link del feed
	 * @return String
     */
    public String getLink() {
        return link;
    }

    /**
     * Metodo per impostare il link del feed
     * @param link Url del feed in stringa
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * Metodo per ottenere il body del feed RSS
     * @return documento HTML
     *
     * @see org.jsoup.nodes.Document Document
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * Metodo per impostare il body del feed RSS
     * @param doc documento HTMl
     *
     * @see org.jsoup.nodes.Document Document
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }
}
