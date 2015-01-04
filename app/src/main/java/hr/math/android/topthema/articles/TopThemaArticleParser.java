package hr.math.android.topthema.articles;

import java.io.IOException;

import org.jsoup.nodes.Document;

import hr.math.android.topthema.Utilities;
import hr.math.android.topthema.articles.TopThemaArticle;

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
     * @throws IOException
     */
    public static TopThemaArticle getArticleFromPage(String link) throws IOException {
        Document rootDocument = Utilities.retrieveHtmlDocument(link);

        String title = getTitle(rootDocument);
        String intro = getIntro(rootDocument);
        String longText = getLongText(rootDocument);
        String mp3Link = getMp3Link(rootDocument);
        String date = getDate(rootDocument);

        return new TopThemaArticle(title, intro, longText, mp3Link, date);
    }


    private static String getTitle(Document rootDocument) {
        String title = rootDocument.getElementsByTag("title").get(0).text();
        return title.substring(0, title.indexOf("| "));
    }

    private static String getIntro(Document rootDocument) {
        return rootDocument.getElementsByClass("intro").get(0).toString();
    }

    private static String getLongText(Document rootDocument) {
        return rootDocument.getElementsByClass("longText").get(0).toString();
    }

    private static String getMp3Link(Document rootDocument) {
        return rootDocument.getElementsByAttributeValue("name", "file_name").attr("value");
    }

    private static String getDate(Document rootDocument) {
        String title = rootDocument.getElementsByTag("title").get(0).text();
        String date = title.substring(title.lastIndexOf("| ") + 2);
        return date;
    }
}
