package hr.math.android.topthema;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hr.math.android.topthema.articles.TopThemaArticle;


public class ArticleActivity extends ActionBarActivity {

    private boolean downloaded;
    private MediaPlayer mp;
    private File file;
    private File dst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        String title = getIntent().getExtras().getString("title");
        final String link = getIntent().getExtras().getString("mp3link");
        final String editedTitle = title.trim().replaceAll(" ", "_") + ".mp3";
        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        file = new File(path, editedTitle);

        final File folder = new File(Environment.getExternalStorageDirectory() + "/TopThemaMedia/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        dst = new File(folder, editedTitle);


        this.setTitle(title);

        TextView textTV = (TextView) findViewById(R.id.textTV);
        String text = getIntent().getExtras().getString("text");
        textTV.setText(Html.fromHtml(text));

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton1);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mp != null && mp.isPlaying()) {
                    mp.stop();
                    return;
                }

                if (!dst.exists()) {
                    String[] children = folder.list();
                    if (children.length > 10) {
                        List<File> childrenFiles = new ArrayList<File>();
                        for (int i = 0; i < 10; i++) {
                            File child = new File(folder + children[i]);
                            childrenFiles.add(child);
                        }
                        Collections.sort(childrenFiles, new Comparator<File>() {
                            @Override
                            public int compare(File lhs, File rhs) {
                                if (lhs.lastModified() < rhs.lastModified()) {
                                    return -1;
                                }
                                if (lhs.lastModified() > rhs.lastModified()) {
                                    return 1;
                                }
                                return 0;
                            }
                        });
                        childrenFiles.get(0).delete();
                    }
                    Utilities.saveMP3(link, editedTitle, ArticleActivity.this);
                }

                if (dst != null && dst.exists()) {
                    AsyncTask<Void, Void, Void> bs = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {

                            mp = MediaPlayer.create(ArticleActivity.this, Uri.fromFile(dst));
                            mp.setVolume(100, 100);
                            mp.start();
                            return null;
                        }
                    };
                    bs.execute();
                }
            }
        });


        registerReceiver(copyAfterDownload,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    BroadcastReceiver copyAfterDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                Utilities.copyFile(file, dst);
            } catch (IOException e) {
                Log.w("myApp", "exception when copying files to internal storage");
            }
            if (file.delete()) {
                Log.w("myApp", "file deleted");
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(copyAfterDownload);
    }
}
