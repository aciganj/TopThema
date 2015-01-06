package hr.math.android.topthema.articles;

import hr.math.android.topthema.Utilities;

import java.io.IOException;
import java.util.ArrayList;
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

            TopThemaArticle article = new TopThemaArticle(URI, title, description);
            if (fullInfo) {
                Document document = Utilities.retrieveHtmlDocument(URI);
                String longText = TopThemaDownloader.getLongText(document);
                String mp3Link = TopThemaDownloader.getMp3Link(document);
                article = new TopThemaArticle(URI, title, description, longText, mp3Link);
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

    @Override
    public void addArticleDownloadedListener(ArticleDownloadedListener listener) {
        listeners.add(listener);
    }

//	private static String getDate(Document rootDocument) {
//		String title = rootDocument.getElementsByTag("title").get(0).text();
//		String date = title.substring(title.lastIndexOf("| ") + 2);
//		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
//		Date date1 = null;
//		try {
//			date1 = formatter.parse(date);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return date;
//	}
}
