package hr.math.android.topthema.articles;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Scraps valuable info from a website.
 *
 * @author kosani
 */
public interface ISiteScraper {
    /**
     * Downloads all articles from the given archiveURL.
     *
     * @param archiveURL
     * @param fullInfo {@value true} if we want to download longtext and mp3link too.
     * @return all articles from the given archiveURL.
     * @throws IOException
     */
	List<TopThemaArticle> getArchiveArticles(String archiveURL, boolean fullInfo) throws IOException;

    /**
     * Downloads all articles from the given index of a archive (indexes go 0 for 2007 to 8 for 2015).
     *
     * @param archiveIndex
     * @param fullInfo {@value true} if we want to download longtext and mp3link too.
     * @return all articles from the given index of a archive (indexes go 0 for 2007 to 8 for 2015).
     * @throws IOException
     */
    List<TopThemaArticle> getArchiveArticles(int archiveIndex, boolean fullInfo) throws IOException;

    /**
     * Downloads all articles from all archives.
     *
     * @param fullInfo {@value true} if we want to download longtext and mp3link too.
     * @return all articles from all archives.
     * @throws IOException
     */
	List<TopThemaArticle> getAllArticles(boolean fullInfo) throws IOException;

    /**
     * Downloads all articles that are newer than the given date.
     * @param date
     * @param fullInfo
     * @return all articles that are newer than the given date.
     * @throws IOException
     */
    List<TopThemaArticle> getArticlesFromDate(Date date, boolean fullInfo) throws IOException;

    /**
     * Adds an {@code ArticleDownloadedListener} to the scraper.
     * @param listener
     */
    void addArticleDownloadedListener(ArticleDownloadedListener listener);
}
