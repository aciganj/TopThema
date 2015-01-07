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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import hr.math.android.topthema.articles.TopThemaArticle;


public class ArticleActivity extends ActionBarActivity {

    private boolean downloaded;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        TextView titleTV = (TextView) findViewById(R.id.titleTV);
        String title = getIntent().getExtras().getString("title");
        titleTV.setText(title);

        TextView textTV = (TextView) findViewById(R.id.textTV);
        String intro = getIntent().getExtras().getString("intro");
        String text = getIntent().getExtras().getString("text");
        textTV.setText(Html.fromHtml(intro + text));

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton1);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (mp != null && mp.isPlaying()) {
                    mp.stop();
                    return;
                }
                //TODO currently saves mp3 to external storage
                String title = getIntent().getExtras().getString("title");
                String link = getIntent().getExtras().getString("mp3link");
                final String editedTitle = title.trim().replaceAll(" ", "_") + ".mp3";
                final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                final File file = new File(path, editedTitle);

                if (!file.exists()) {
                    Utilities.saveMP3(link, editedTitle, ArticleActivity.this);
                }

                if (file.exists()) {
                    AsyncTask<Void, Void, Void> bs = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {

                            mp = MediaPlayer.create(ArticleActivity.this, Uri.parse(path + "/" + editedTitle));

                            mp.setVolume(100, 100);
                            mp.start();

                            return null;
                        }
                    };


                    bs.execute();

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
