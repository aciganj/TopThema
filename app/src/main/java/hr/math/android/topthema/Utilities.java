package hr.math.android.topthema;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
    public static void saveFile(String url, String fileName, Context context) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));


        //fileName without extension
        String title = fileName.substring(0, fileName.lastIndexOf('.'));
        request.setTitle(title);
        request.setDescription("One of Top-Thema files");//TODO

        //scans for meta data to allow queries... no need?
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //save location
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    /**
     * Saves text to file.
     *
     * @param text     text to be saved
     * @param fileName name of the file, must contain extension
     */
    public static void saveText(String text, String fileName, Context context) {
        try {
            FileOutputStream fOut = context.openFileOutput(fileName, context.MODE_PRIVATE);

            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            //write string to file
            osw.write(text);
            osw.flush();
            osw.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
