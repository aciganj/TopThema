package hr.math.android.topthema.articles;

import hr.math.android.topthema.Utilities;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TopThemaScraper implements ISiteScraper {
    private static final String BASE_URL = "http://www.dw.de/";
    private static final String TOP_THEMA_URL = "http://www.dw.de/deutsch-lernen/top-thema/s-8031";
    private List<ArticleDownloadedListener> listeners = new ArrayList<>();

    @Override
    public List<TopThemaArticle> getArchiveArticles(String archiveURL, boolean fullInfo) throws IOException {
        List<TopThemaArticle> listOfArticles = new ArrayList<TopThemaArticle>();
        Document archiveDocument = Utilities.retrieveHtmlDocument(archiveURL);

        List<Element> elementsWithLinks = archiveDocument.getElementsByAttributeValue("class", "linkList intern");
        for (Element element : elementsWithLinks) {
            String URI = BASE_URL + element.getElementsByTag("a").attr("href").trim();
            String title = element.getElementsByTag("h2").text().trim();
            String description = element.getElementsByTag("p").text().trim();
            Date date = getDate(description);

            TopThemaArticle article = new TopThemaArticle(URI, title, description, date);
            if (fullInfo) {
                Document articleDocument = Utilities.retrieveHtmlDocument(URI);
                String longText = getLongText(articleDocument);
                String mp3Link = getMp3Link(articleDocument);
                article = new TopThemaArticle(URI, title, description, longText, mp3Link, date);
            }
            System.out.println("Adding: " + article);
            listOfArticles.add(article);
            notify(article);
        }
        return listOfArticles;
    }

    private void notify(TopThemaArticle article) {
        for (ArticleDownloadedListener listener : listeners) {
            listener.onArticleDownloaded(article);
        }
    }

    @Override
    public List<TopThemaArticle> getAllArticles(boolean fullInfo) throws IOException {
        List<TopThemaArticle> listOfArticles = new ArrayList<TopThemaArticle>();
        Document rootDocument = Utilities.retrieveHtmlDocument(TOP_THEMA_URL);

        List<Element> elementsWithLinks = rootDocument.getElementsByAttributeValue("class", "linkList intern");
        for (Element element : elementsWithLinks) {
            String URI = BASE_URL + element.getElementsByTag("a").attr("href").trim();
            listOfArticles.addAll(getArchiveArticles(URI, fullInfo));
        }
        return listOfArticles;
    }

    private static String getLongText(Document rootDocument) {
        return rootDocument.getElementsByClass("intro").get(0).toString()
                + rootDocument.getElementsByClass("longText").get(0).toString();
    }

    private static String getMp3Link(Document rootDocument) {
        return rootDocument.getElementsByAttributeValue("name", "file_name").attr("value");
    }

    private static Date getDate(String description) {
        String dateString = MonthParser.parseDate(description);
        DateFormat formatter = new SimpleDateFormat("dd MM yyyy");
        Date date = null;
        try {
            if (dateString == null) {
                date = formatter.parse("01 01 2007");
            } else {
                date = formatter.parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void addArticleDownloadedListener(ArticleDownloadedListener listener) {
        listeners.add(listener);
    }

}