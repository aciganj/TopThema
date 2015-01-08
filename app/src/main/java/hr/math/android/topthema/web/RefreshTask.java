package hr.math.android.topthema.web;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import hr.math.android.topthema.DAO.DAO;
import hr.math.android.topthema.articles.ArticleAdapter;
import hr.math.android.topthema.articles.ISiteScraper;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.articles.TopThemaScraper;

/**
 * Created by kosani on 1/8/15.
 */
public class RefreshTask extends AsyncTask<Date, Void, TopThemaArticle> {

    private Activity callerActivity;
    private DAO dao;
    private List<TopThemaArticle> articlesOnScreen;
    private int articlesOnScreenNum;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleAdapter topThemaAdapter;

    public RefreshTask(Activity callerActivity, DAO dao, List<TopThemaArticle> articlesOnScreen,
                       int articlesOnScreenNum, SwipeRefreshLayout mSwipeRefreshLayout, ArticleAdapter topThemaAdapter) {
        this.callerActivity = callerActivity;
        this.dao = dao;
        this.articlesOnScreen = articlesOnScreen;
        this.articlesOnScreenNum = articlesOnScreenNum;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.topThemaAdapter = topThemaAdapter;
    }

    @Override
    protected TopThemaArticle doInBackground(Date... params) {
        Date date = params[0];
        ISiteScraper siteScraper;
        try {
            siteScraper = new TopThemaScraper();
            List<TopThemaArticle> newArticles = siteScraper.getArticlesFromDate(date, false);
            dao.save(newArticles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(TopThemaArticle article) {
        articlesOnScreen.clear();
        articlesOnScreen.addAll(dao.getLatest(articlesOnScreenNum));
        mSwipeRefreshLayout.setRefreshing(false);
        topThemaAdapter.notifyDataSetChanged();
    }
}
