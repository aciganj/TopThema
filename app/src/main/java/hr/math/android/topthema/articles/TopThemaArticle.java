package hr.math.android.topthema.articles;

import com.orm.SugarRecord;

import org.jsoup.nodes.Document;

import java.io.IOException;

import hr.math.android.topthema.Utilities;

/**
 * Represents an article of the top thema category.
 * 
 * @author Alen Kosanović
 *
 */
public class TopThemaArticle extends SugarRecord<TopThemaArticle> {
	private String URI;
	private String title;
	private String description;
	private String longText = "";
	private String mp3Link = "";
	
	public TopThemaArticle() {
	}
	
	public TopThemaArticle(String uRI, String title, String description) {
		super();
		URI = uRI;
		this.title = title;
		this.description = description;
	}

	public TopThemaArticle(String uRI, String title, String description, String longText, String mp3Link) {
		super();
		URI = uRI;
		this.title = title;
		this.description = description;
		this.longText = longText;
		this.mp3Link = mp3Link;
	}

	public void stripArticle() {
		longText = null;
		mp3Link = null;
	}
	
	/**
	 * @return the uRI
	 */
	public String getURI() {
		return URI;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the longText
	 */
	public String getLongText() throws IOException {
		if (longText == "") {
            Document document = Utilities.retrieveHtmlDocument(URI);
            longText = TopThemaDownloader.getLongText(document);
            mp3Link = TopThemaDownloader.getMp3Link(document);
        }
        return longText;
	}

	/**
	 * @return the mp3Link
	 */
	public String getMp3Link() throws IOException {
        if (mp3Link == "") {
            Document document = Utilities.retrieveHtmlDocument(URI);
            longText = TopThemaDownloader.getLongText(document);
            mp3Link = TopThemaDownloader.getMp3Link(document);
        }
        return mp3Link;
	}
	
	public void strip() {
		mp3Link = null;
		longText = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return title;
	}
}
