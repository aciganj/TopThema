package hr.math.android.topthema;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.math.android.topthema.articles.ArticleDownloadedListener;
import hr.math.android.topthema.articles.IArticleDownloader;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.articles.TopThemaArticleDownloader;


public class MainActivity extends ListActivity {
    /**
     * An {@link android.widget.ArrayAdapter} that holds a reference to {@link #articles}.
     */
    private ArrayAdapter<TopThemaArticle> topThemaAdapter;
    /**
     * All the articles that were downloaded.
     */
    private List<TopThemaArticle> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        articles = new ArrayList<>();
        topThemaAdapter = new ArrayAdapter<TopThemaArticle>(MainActivity.this, R.layout.list_item, articles);
        setListAdapter(topThemaAdapter);
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
            IArticleDownloader articleList = null;
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
            };
            return null;
        }

        // Since UI updates must be made only from the UI thread, we need to call the notifyDataSetChanged
        // inside this method (which allows modifying the UI).
        @Override
        protected void onProgressUpdate(TopThemaArticle... values) {
            topThemaAdapter.notifyDataSetChanged();
        }
    }
}
