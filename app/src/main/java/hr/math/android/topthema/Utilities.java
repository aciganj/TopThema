package hr.math.android.topthema;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;
import java.nio.channels.FileChannel;

/**
 * Temporary class. TODO:Should be tested
 */
public class Utilities {


    /**
     * Downloads and saves file from give url.
     *
     * @param url      url from which the file will be downloaded
     * @param fileName name of the file, must contain extension
     */
    public static void saveMP3(String url, String fileName, Context context) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setTitle(fileName.substring(0, fileName.lastIndexOf('.')));
        request.setDescription("Top-Thema mp3 file");

        //scans for meta data to allow queries... no need?
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //save location
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        //get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    /**
     * Copies file.
     *
     * @param src source file
     * @param dst destination file
     * @throws IOException
     */
    public static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    /**
     * Retrieves a HTML document as {@link org.jsoup.nodes.Document} from the given link.
     *
     * @param link - the given link
     * @return {@link org.jsoup.nodes.Document} of the given HTML link
     * @throws java.io.IOException
     */
    public static Document retrieveHtmlDocument(String link) throws IOException {
        link = StringEscapeUtils.escapeHtml4(link);
        Document htmlDocument = null;
        boolean documentRetrieved = false;

        //in case an exception occurs.
        while (!documentRetrieved) {
            htmlDocument = Jsoup.connect(link).get();
            documentRetrieved = true;
        }
        return htmlDocument;
    }
}
