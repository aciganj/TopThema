package hr.math.android.topthema;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hr.math.android.topthema.DAO.DAO;
import hr.math.android.topthema.DAO.DAOProvider;
import hr.math.android.topthema.articles.ArticleAdapter;
import hr.math.android.topthema.articles.TopThemaArticle;
import hr.math.android.topthema.web.DownloadArticleTask;
import hr.math.android.topthema.web.InitializerTask;
import hr.math.android.topthema.web.RefreshTask;

public class MainActivity extends ActionBarActivity {
    /**
     * An {@link hr.math.android.topthema.articles.ArticleAdapter} that holds a reference to {@link #articlesOnScreen}.
     */
    private ArticleAdapter topThemaAdapter;
    /**
     * All the articles that were downloaded.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * {@link hr.math.android.topthema.articles.TopThemaArticle} displayed on the screen.
     */
    private List<TopThemaArticle> articlesOnScreen;
    private DAO dao;
    /**
     * Number of {@link hr.math.android.topthema.articles.TopThemaArticle}s displayed.
     */
    private int articlesOnScreenNum = 10;
    /**
     * Item click listener for listView. Starts activity for contact details.
     */
    private OnItemClickListener articleListListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TopThemaArticle article = articlesOnScreen.get(position);
            if (article.isStripped()) {
                new DownloadArticleTask(MainActivity.this).execute(article);
            } else {
                startSecondActivity(article);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = DAOProvider.getDAO();

        articlesOnScreen = new ArrayList<>();
        topThemaAdapter = new ArticleAdapter(MainActivity.this, (ArrayList<TopThemaArticle>) articlesOnScreen);

        ListView listView = (ListView) findViewById(R.id.articleListView);
        listView.setAdapter(topThemaAdapter);
        listView.setOnItemClickListener(articleListListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Date latestDate = getLatestArticle();
                new RefreshTask(MainActivity.this, dao, articlesOnScreen, articlesOnScreenNum,
                        mSwipeRefreshLayout, topThemaAdapter).execute(latestDate);
            }
        });

        TextView refreshTV = new TextView(this);
        refreshTV.setText("Swipe to refresh");
        refreshTV.setGravity(View.TEXT_ALIGNMENT_CENTER);
        listView.addHeaderView(refreshTV);

        Button loadMoreButton = new Button(this);
        loadMoreButton.setText("Load more");
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articlesOnScreenNum += 10;
                List<TopThemaArticle> newArticles = dao.getLatest(articlesOnScreenNum);
                articlesOnScreen.clear();
                articlesOnScreen.addAll(newArticles);
                topThemaAdapter.notifyDataSetChanged();
            }
        });
        listView.addFooterView(loadMoreButton);

        if (!dao.isDatabaseInstantiated()) {
            new InitializerTask(MainActivity.this, dao, articlesOnScreen, articlesOnScreenNum, topThemaAdapter).execute();
        } else {
            dao.deleteLast(3);
            articlesOnScreen.addAll(dao.getLatest(articlesOnScreenNum));
            topThemaAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Date getLatestArticle() {
        if (!articlesOnScreen.isEmpty()) {
            return articlesOnScreen.get(0).getDate();
        } else {
            return null;
        }
    }

    public void startSecondActivity(TopThemaArticle article) {
        if (!article.isStripped()) {
            //TODO refactor (repeating twice)
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("title", article.getTitle());
            intent.putExtra("text", article.getLongText());
            intent.putExtra("mp3link", article.getMp3Link());
            intent.putExtra("URI", article.getURI());
            intent.putExtra("date", article.getDate());
            intent.putExtra("description", article.getDescription());
            startActivity(intent);
        }
    }
}
