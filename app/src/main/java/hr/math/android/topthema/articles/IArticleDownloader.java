package hr.math.android.topthema.articles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An interface towards fetching articles from the internet.
 * 
 * @author Alen Kosanović
 *
 */
public interface IArticleDownloader {
	/**
	 * Fetches only the latest top thema articles from the internet.
	 * @return the latest top thema articles from the internet
	 * @throws java.io.IOException
	 */
	List<TopThemaArticle> getLatestArticles() throws IOException;

    /**
     * Adds an listener that is activated when the {@link IArticleDownloader} downloads an article.
     * @param listener
     */
    void addArticleDownloadedListener(ArticleDownloadedListener listener);
}
