package hr.math.android.topthema;

import android.app.DownloadManager;
import android.app.ListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import hr.math.android.topthema.articles.IArticleList;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.articles.TopThemaArticleList;


public class MainActivity extends ListActivity {

    private ArrayAdapter<TopThemaArticle> topThemaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("On create");
        super.onCreate(savedInstanceState);

        setListAdapter(topThemaAdapter);

        new DownloadArticlesTask().execute();

    }

    private class DownloadArticlesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            IArticleList articleList = null;
            try {
                articleList = new TopThemaArticleList();
                topThemaAdapter = new ArrayAdapter<TopThemaArticle>(MainActivity.this, R.layout.list_item,
                        articleList.getLatestArticles());
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
