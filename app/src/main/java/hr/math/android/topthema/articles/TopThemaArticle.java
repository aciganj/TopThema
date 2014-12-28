package hr.math.android.topthema.articles;

/**
 * Represents an article of the top thema category.
 * 
 * @author Alen Kosanović
 *
 */
public class TopThemaArticle {
	private String title;
	private String intro;
	private String longText;
	private String mp3Link;
	
	public TopThemaArticle(String title, String intro, String longText, String mp3Link) {
		super();
		this.title = title;
		this.intro = intro;
		this.longText = longText;
		this.mp3Link = mp3Link;
	}
	/**
	 * @return the #title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the #intro
	 */
	public String getIntro() {
		return intro;
	}
	/**
	 * @return the #longText
	 */
	public String getLongText() {
		return longText;
	}
	/**
	 * @return the #mp3Link
	 */
	public String getMp3Link() {
		return mp3Link;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TopThemaArticle [title=" + title + ", mp3Link=" + mp3Link + "]";
	}
}
