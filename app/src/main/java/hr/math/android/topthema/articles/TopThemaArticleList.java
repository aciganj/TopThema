package hr.math.android.topthema.articles;

import hr.math.android.topthema.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * An article list for the "Top thema" category.
 * 
 * @author Alen Kosanović
 *
 */
public class TopThemaArticleList implements IArticleList {
    /**
     * A URL that redirects to the top thema website.
     */
	private static final String TOP_THEMA_ROOT_LINK = "http://www.dw.de/deutsch-lernen/top-thema/s-8031";

	private Document rootDocument;

	public TopThemaArticleList() throws IOException {
		rootDocument = Utilities.retrieveHtmlDocument(TOP_THEMA_ROOT_LINK);
	}

    /**
     * @return only the latest articles on the top-thema.The rest of them can be accessed via the archive.
     * @throws IOException
     */
	public List<TopThemaArticle> getLatestArticles() throws IOException {
		List<TopThemaArticle> listOfArticles = new ArrayList<TopThemaArticle>();
		
		for (int i = 0; i < possibleNumberOfArticles(); i++) {
			String articleLink = getArticleLinkAtIndex(i);
			if (!articleLink.isEmpty()) {
				TopThemaArticle newArticle = TopThemaArticleParser.getArticleFromPage(articleLink);
				listOfArticles.add(newArticle);
				System.out.println("Added new article: " + newArticle);
			}
		}	
		return listOfArticles;
	}

	private String getArticleLinkAtIndex(int i) {
		List<Element> elementsWithLinks = rootDocument.getElementsByAttributeValue("class", "news");
        // We still have to check if it contains a link.
		if (elementsWithLinks.get(i).getElementsByTag("a") != null) {
			return "http://www.dw.de" + elementsWithLinks.get(i).getElementsByTag("a").get(0).attr("href").trim();
		} else {
			return "";
		}
	}

    /**
     * Estimates the possible number of articles. Not all classes of value {@value news} are articles.
     * Some of them do not contain a link (an <!-- <a></a> --> tag inside), therefore, a check is needed
     * outside of the method.
     *
     * @return number of possible articles.
     */
	private int possibleNumberOfArticles() {
		return rootDocument.getElementsByAttributeValue("class", "news").size();
	}
}
