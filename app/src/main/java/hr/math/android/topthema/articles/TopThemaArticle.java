package hr.math.android.topthema.articles;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;

import hr.math.android.topthema.Utilities;

/**
 * Represents an article of the top thema category.
 * 
 * @author Alen Kosanović
 *
 */
@Table(name = "TopThema")
public class TopThemaArticle extends Model {
    @Column(name = "URI")
    private String URI;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "longtext")
    private String longText;
    @Column(name = "mp3link")
    private String mp3Link;
    @Column(name = "date")
    private Date date;

    public TopThemaArticle() {
    }

    public TopThemaArticle(String uRI, String title, String description, Date date) {
        super();
        URI = uRI;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public TopThemaArticle(String uRI, String title, String description, String longText, String mp3Link, Date date) {
        super();
        URI = uRI;
        this.title = title;
        this.description = description;
        this.longText = longText;
        this.mp3Link = mp3Link;
        this.date = date;
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
    public String getLongText() {
        return longText;
    }

    /**
     * @return the mp3Link
     */
    public String getMp3Link() {
        return mp3Link;
    }

    public Date getDate() {
        return date;
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
