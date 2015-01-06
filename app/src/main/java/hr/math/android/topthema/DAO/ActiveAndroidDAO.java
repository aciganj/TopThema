package hr.math.android.topthema.DAO;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Arrays;
import java.util.List;

import hr.math.android.topthema.articles.TopThemaArticle;

/**
 * Created by kosani on 1/6/15.
 */
public class ActiveAndroidDAO implements DAO {
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
                    articles.get(bulk * bulkSize + i).save();
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
    public List<TopThemaArticle> loadAllArticles() {
        return new Select().all().from(TopThemaArticle.class).execute();
    }
}
