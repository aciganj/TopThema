package hr.math.android.topthema.articles;

/**
 * A listener that is activated when an article is downloaded.
 *
 * @author kosani
 *
 */
public interface ArticleDownloadedListener {
    /**
     * The notify method in the Listener design pattern.
     * @param newArticle
     */
    void onArticleDownloaded(TopThemaArticle newArticle);
}
