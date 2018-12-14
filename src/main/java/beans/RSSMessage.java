package beans;

import org.jsoup.nodes.Document;

public class RSSMessage {
    private String title;
    private String link;
    private Document doc;

    public RSSMessage(String title, String link, Document doc){
        setTitle(title);
        setLink(link);
        setDoc(doc);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }
}
