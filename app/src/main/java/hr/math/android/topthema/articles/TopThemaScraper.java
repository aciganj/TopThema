package hr.math.android.topthema.articles;

import hr.pmf.math.android.topthema.utils.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TopThemaScraper implements ISiteScraper {
	private static final String BASE_URL = "http://www.dw.de/";
	private static final String TOP_THEMA_URL = "http://www.dw.de/deutsch-lernen/top-thema/s-8031";

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
				Document articleDocument = Utilities.retrieveHtmlDocument(URI);
				String longText = getLongText(articleDocument);
				String mp3Link = getMp3Link(articleDocument);
				article = new TopThemaArticle(URI, title, description, longText, mp3Link);
			}
			System.out.println("Adding: " + article);
			listOfArticles.add(article);
		}
		return listOfArticles;
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
