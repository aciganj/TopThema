package hr.math.android.topthema.DAO;

import java.util.Date;
import java.util.List;

import hr.math.android.topthema.articles.TopThemaArticle;

/**
 * Created by kosani on 1/6/15.
 */
public interface DAO {
    boolean isDatabaseInstantiated();
    void save(List<TopThemaArticle> articles);
    void save(TopThemaArticle[] articles);
    void save(TopThemaArticle article);
    List<TopThemaArticle> loadAllArticles();
    List<TopThemaArticle> loadLatest(int num);
    void deleteAll();
    void deleteLast(int num);
}
