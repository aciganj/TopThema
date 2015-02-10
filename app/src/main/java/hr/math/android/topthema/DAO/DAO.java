package hr.math.android.topthema.DAO;

import java.util.Date;
import java.util.List;

import hr.math.android.topthema.articles.TopThemaArticle;

/**
 * Created by kosani on 1/6/15.
 */
public interface DAO {
    /**
     * @return {@value true} if database is instantiated or {@value false} otherwise.
     */
    boolean isDatabaseInstantiated();

    /**
     * @param articles list of {@link hr.math.android.topthema.articles.TopThemaArticle} to be saved.
     */
    void save(List<TopThemaArticle> articles);

    /**
     * @param articles array of {@link hr.math.android.topthema.articles.TopThemaArticle} to be saved.
     */
    void save(TopThemaArticle[] articles);

    /**
     * @param article {@link hr.math.android.topthema.articles.TopThemaArticle} to be saved.
     */
    void save(TopThemaArticle article);

    /**
     * @return all {@link hr.math.android.topthema.articles.TopThemaArticle}s from the database.
     */
    List<TopThemaArticle> getAllArticles();

    /**
     * @param num number of {@link hr.math.android.topthema.articles.TopThemaArticle}s to load.
     * @return num latest {@link hr.math.android.topthema.articles.TopThemaArticle}s
     */
    List<TopThemaArticle> getLatest(int num);

    /**
     * @return all {@link hr.math.android.topthema.articles.TopThemaArticle}s with some info.
     */
    List<TopThemaArticle> getArticlesWithFullInfo();

    /**
     * @return all {@link hr.math.android.topthema.articles.TopThemaArticle}s with mp3s.
     */
    List<TopThemaArticle> getArticlesWithMP3();

    /**
     * Used only for testing. Deletes all articles.
     */
    void deleteAll();

    /**
     * Used only for testing. Deletes last num articles.
     *
     * @param num
     */
    void deleteLast(int num);

    /**
     * Deletes articles with the given URI
     *
     * @param URI
     */
    void delete(String URI);
}
