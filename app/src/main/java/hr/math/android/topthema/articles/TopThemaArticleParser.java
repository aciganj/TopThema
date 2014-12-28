package hr.math.android.topthema.articles;

import java.io.IOException;

import hr.math.android.topthema.Utilities;

import org.jsoup.nodes.Document;

/**
 * A non-instanitiable class that provides parsing abilities to the user.
 * 
 * @author Alen Kosanović
 *
 */
public class TopThemaArticleParser {
	// non-instanitiable
	private TopThemaArticleParser() {}

	/**
	 * Returns an {@link TopThemaArticle} instance from a given String
	 * 
	 * @param link URL as String
	 * @return {@link TopThemaArticle} instance of the given URL.
	 * @throws java.io.IOException
	 */
	public static TopThemaArticle getArticleFromPage(String link) throws IOException {
		Document rootDocument = Utilities.retrieveHtmlDocument(link);
		
		String title = getTitle(rootDocument);
		String intro = getIntro(rootDocument);
		String longText = getLongText(rootDocument);
		String mp3Link = getMp3Link(rootDocument);

		return new TopThemaArticle(title, intro, longText, mp3Link);
	}

	private static String getTitle(Document rootDocument) {
		String titleWithDash = rootDocument.getElementsByAttributeValue("name", "media_title").attr("value");
		// Remove the  – das Top-Thema als MP3 from the title
		String title = titleWithDash.substring(0, titleWithDash.indexOf('–') - 1);
		return title;
	}

	private static String getIntro(Document rootDocument) {
		return rootDocument.getElementsByClass("intro").get(0).text();
	}

	private static String getLongText(Document rootDocument) {
		return rootDocument.getElementsByClass("longText").get(0).text();
	}

	private static String getMp3Link(Document rootDocument) {
		return rootDocument.getElementsByAttributeValue("name", "file_name").attr("value");
	}
}
