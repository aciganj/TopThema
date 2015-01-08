package hr.math.android.topthema.web;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import hr.math.android.topthema.ArticleActivity;
import hr.math.android.topthema.articles.TopThemaArticle;

/**
 * Created by kosani on 1/8/15.
 */
public class DownloadArticleTask extends AsyncTask<TopThemaArticle, Void, TopThemaArticle> {

    private Activity callerActivity;

    public DownloadArticleTask(Activity callerActivity) {
        this.callerActivity = callerActivity;
    }

    @Override
    protected TopThemaArticle doInBackground(TopThemaArticle... params) {
        TopThemaArticle article = params[0];
        try {
            article.downloadFullData();
            article.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return article;
    }

    @Override
    protected void onPostExecute(TopThemaArticle article) {
        if (!article.isStripped()) {
            //TODO refactor
            Intent intent = new Intent(callerActivity, ArticleActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("text", article.getLongText());
            intent.putExtra("mp3link", article.getMp3Link());
            intent.putExtra("URI", article.getURI());
            intent.putExtra("date", article.getDate());
            intent.putExtra("description", article.getDescription());
            callerActivity.startActivity(intent);
        }
    }
}
