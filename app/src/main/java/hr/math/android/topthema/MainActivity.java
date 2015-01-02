package hr.math.android.topthema;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hr.math.android.topthema.articles.IArticleList;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.articles.TopThemaArticleList;


public class MainActivity extends ListActivity {

    private ArrayAdapter<TopThemaArticle> topThemaAdapter;
    private List<TopThemaArticle> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        articles = new ArrayList<>();
        topThemaAdapter = new ArrayAdapter<TopThemaArticle>(MainActivity.this, R.layout.list_item, articles);
        setListAdapter(topThemaAdapter);
        new DownloadArticlesTask().execute();

    }


    private class DownloadArticlesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            IArticleList articleList = null;
            try {
                articleList = new TopThemaArticleList();
                articles.addAll(articleList.getLatestArticles());
            } catch (IOException e) {
                e.printStackTrace();
            };
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setListAdapter(topThemaAdapter);
            topThemaAdapter.notifyDataSetChanged();
            System.out.println("Notifying data set changed");
        }
    }
}
