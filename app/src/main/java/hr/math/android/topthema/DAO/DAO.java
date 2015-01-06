package hr.math.android.topthema.DAO;

import java.util.List;

import hr.math.android.topthema.articles.TopThemaArticle;

/**
 * Created by kosani on 1/6/15.
 */
public interface DAO {
    void save(List<TopThemaArticle> articles);
    void save(TopThemaArticle[] articles);
    List<TopThemaArticle> loadAllArticles();
}
