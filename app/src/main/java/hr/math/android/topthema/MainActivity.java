package hr.math.android.topthema;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import hr.math.android.topthema.articles.ArticleAdapter;
import hr.math.android.topthema.articles.ArticleDownloadedListener;
import hr.math.android.topthema.articles.IArticleDownloader;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.articles.TopThemaArticleDownloader;

public class MainActivity extends Activity {
    /**
     * An {@link hr.math.android.topthema.articles.ArticleAdapter} that holds a reference to {@link #articles}.
     */
    private ArticleAdapter topThemaAdapter;
    /**
     * All the articles that were downloaded.
     */
    private ArrayList<TopThemaArticle> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articles = new ArrayList<>();
        topThemaAdapter = new ArticleAdapter(MainActivity.this, articles);

        ListView listView = (ListView) findViewById(R.id.articleListView);
        listView.setAdapter(topThemaAdapter);
        listView.setOnItemClickListener(articleListListener);

        new DownloadArticlesTask().execute();
    }

    /**
     * Http operations cannot be executed in a UI Thread, therefore, an {@link android.os.AsyncTask}
     * is called that does the internet job.
     *
     * @author kosani
     */
    private class DownloadArticlesTask extends AsyncTask<Void, TopThemaArticle, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            IArticleDownloader articleList;
            try {
                articleList = new TopThemaArticleDownloader();
                articleList.addArticleDownloadedListener(new ArticleDownloadedListener() {

                    @Override
                    public void onArticleDownloaded(TopThemaArticle newArticle) {
                        articles.add(newArticle);
                        publishProgress(newArticle);
                    }
                });
                articleList.getLatestArticles();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Since UI updates must be made only from the UI thread, we need to call the notifyDataSetChanged
        // inside this method (which allows modifying the UI).
        @Override
        protected void onProgressUpdate(TopThemaArticle... values) {
            topThemaAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Item click listener for listView. Starts activity for contact details.
     */
    private OnItemClickListener articleListListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
            intent.putExtra("title", articles.get(position).getTitle());
            intent.putExtra("intro", articles.get(position).getIntro());
            intent.putExtra("text", articles.get(position).getLongText());
            intent.putExtra("mp3link", articles.get(position).getMp3Link());
            startActivity(intent);

        }
    };
}
