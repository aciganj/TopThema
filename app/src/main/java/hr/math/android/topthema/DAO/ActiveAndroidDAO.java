package hr.math.android.topthema.DAO;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.Arrays;
import java.util.List;

import hr.math.android.topthema.articles.TopThemaArticle;

/**
 * Created by kosani on 1/6/15.
 */
public class ActiveAndroidDAO implements DAO {
    @Override
    public boolean isDatabaseInstantiated() {
        List<TopThemaArticle> list = loadLatest(1);
        return list != null && !list.isEmpty();
    }

    @Override
    public void save(List<TopThemaArticle> articles) {
        int size = articles.size();
        // + 1 because we can not have 0 bulks.
        int bulkSize = 100;
        int bulksNum = size / bulkSize + 1;
        for (int bulk = 0; bulk < bulksNum; bulk++) {

            ActiveAndroid.beginTransaction();
            try {
                for (int i = 0; i < bulkSize; i++) {
                    if (bulk * bulkSize + i == size) {
                        break;
                    }
                    TopThemaArticle article = articles.get(bulk * bulkSize + i);
                    save(article);
                }
                ActiveAndroid.setTransactionSuccessful();
            }
            finally {
                ActiveAndroid.endTransaction();
            }
        }
    }

    @Override
    public void save(TopThemaArticle[] articles) {
        save(Arrays.asList(articles));
    }

    @Override
    public void save(TopThemaArticle article) {
        article.save();
        Log.i("DATABASE", "Saved article - " + article);
    }

    @Override
    public List<TopThemaArticle> loadAllArticles() {
        return new Select().all().from(TopThemaArticle.class).execute();
    }

    @Override
    public List<TopThemaArticle> loadLatest(int num) {
        return new Select().all().from(TopThemaArticle.class).orderBy("date DESC").limit(num).execute();
    }

    @Override
    public void deleteAll() {
        new Delete().from(TopThemaArticle.class).execute();
    }

    @Override
    public void deleteLast(int num) {
        List<TopThemaArticle> articles = loadLatest(3);
        for (TopThemaArticle article : articles) {
            new Delete().from(TopThemaArticle.class).where("URI == ?", article.getURI()).execute();
        }
    }

}