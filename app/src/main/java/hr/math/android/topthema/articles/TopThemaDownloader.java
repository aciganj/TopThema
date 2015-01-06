package hr.math.android.topthema.articles;

import org.jsoup.nodes.Document;

import java.io.IOException;

import hr.math.android.topthema.Utilities;

/**
 * Created by kosani on 1/5/15.
 */
public class TopThemaDownloader {
    private TopThemaDownloader() {}

    public static String getLongText(Document rootDocument) throws IOException {
        return rootDocument.getElementsByClass("intro").get(0).toString()
                + rootDocument.getElementsByClass("longText").get(0).toString();
    }

    public static String getMp3Link(Document rootDocument) throws IOException {
        return rootDocument.getElementsByAttributeValue("name", "file_name").attr("value");
    }
}
