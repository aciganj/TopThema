package hr.math.android.topthema.articles;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ISiteScraper {
	List<TopThemaArticle> getArchiveArticles(String archiveURL, boolean fullInfo) throws IOException;
    List<TopThemaArticle> getArchiveArticles(int archiveIndex, boolean fullInfo) throws IOException;
	List<TopThemaArticle> getAllArticles(boolean fullInfo) throws IOException;
    List<TopThemaArticle> getArticlesFromDate(Date date, boolean fullInfo) throws IOException;

    void addArticleDownloadedListener(ArticleDownloadedListener listener);
}
