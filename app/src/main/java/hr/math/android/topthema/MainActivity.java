package hr.math.android.topthema;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import hr.math.android.topthema.DAO.DAO;
import hr.math.android.topthema.DAO.DAOProvider;
import hr.math.android.topthema.articles.ArticleAdapter;
import hr.math.android.topthema.articles.ArticleDownloadedListener;
import hr.math.android.topthema.articles.ISiteScraper;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.articles.TopThemaScraper;

public class MainActivity extends Activity {
    /**
     * An {@link hr.math.android.topthema.articles.ArticleAdapter} that holds a reference to {@link #articles}.
     */
    private ArticleAdapter topThemaAdapter;
    /**
     * All the articles that were downloaded.
     */
    private ArrayList<TopThemaArticle> articles;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articles = new ArrayList<>();
        Gson gson = new Gson();
        DAO dao = DAOProvider.getDAO();
        InputStream is = null;

        try {
            is = MainActivity.this.getResources().getAssets().open("TopThemaArticles.json");
            TopThemaArticle[] articlesArray = gson.fromJson(new InputStreamReader(is), TopThemaArticle[].class);

            dao.save(articlesArray);

            List<TopThemaArticle> articles1 = dao.loadAllArticles();
            int l = 1;

        } catch (IOException e) {
            e.printStackTrace();
        }

        topThemaAdapter = new ArticleAdapter(MainActivity.this, articles);

        ListView listView = (ListView) findViewById(R.id.articleListView);
        listView.setAdapter(topThemaAdapter);
        listView.setOnItemClickListener(articleListListener);

        //new DownloadArticlesTask().execute();
    }

    private String readEntireJSON() {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = MainActivity.this.getResources().getAssets().open("TopThemaArticles.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (String line = br.readLine(); line != null && !line.isEmpty(); line = br.readLine()) {
                sb.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return sb.toString();
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
            ISiteScraper siteScraper;
            try {
                siteScraper = new TopThemaScraper();
                siteScraper.addArticleDownloadedListener(new ArticleDownloadedListener() {

                    @Override
                    public void onArticleDownloaded(TopThemaArticle newArticle) {
                        articles.add(newArticle);
                        //publishProgress(newArticle);
                    }
                });
                siteScraper.getAllArticles(false);
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

        @Override
        protected void onPostExecute(Void aVoid) {
            topThemaAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Item click listener for listView. Starts activity for contact details.
     */
    private OnItemClickListener articleListListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("title", articles.get(position).getTitle());
            intent.putExtra("text", articles.get(position).getLongText());

            startActivity(intent);

        }
    };
}
