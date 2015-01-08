package hr.math.android.topthema.web;

/**
 * Created by kosani on 1/8/15.
 */

import android.app.Activity;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import hr.math.android.topthema.DAO.DAO;
import hr.math.android.topthema.articles.ArticleAdapter;
import hr.math.android.topthema.articles.ISiteScraper;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.articles.TopThemaScraper;

/**
 * Http operations cannot be executed in a UI Thread, therefore, an {@link android.os.AsyncTask}
 * is called that does the internet job.
 *
 * @author kosani
 */
public class InitializerTask extends AsyncTask<Void, TopThemaArticle, Void> {

    private Activity callerActivity;
    private DAO dao;
    private List<TopThemaArticle> articlesOnScreen;
    private int articlesOnScreenNum;
    private ArticleAdapter topThemaAdapter;

    public InitializerTask(Activity callerActivity, DAO dao, List<TopThemaArticle> articlesOnScreen,
                           int articlesOnScreenNum, ArticleAdapter topThemaAdapter) {
        this.callerActivity = callerActivity;
        this.dao = dao;
        this.articlesOnScreen = articlesOnScreen;
        this.articlesOnScreenNum = articlesOnScreenNum;
        this.topThemaAdapter = topThemaAdapter;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ISiteScraper siteScraper;
        try {
            siteScraper = new TopThemaScraper();
            List<TopThemaArticle> allArticles = siteScraper.getAllArticles(false);
            dao.save(allArticles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        articlesOnScreen.addAll(dao.getLatest(articlesOnScreenNum));
        topThemaAdapter.notifyDataSetChanged();
    }
}
