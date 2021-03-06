package hr.math.android.topthema.articles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hr.math.android.topthema.R;

/**
 * Custom array adapter for top thema articles.
 *
 * @author Andrej Ciganj
 */
public class ArticleAdapter extends ArrayAdapter<TopThemaArticle> {

    /**
     * Constructor.
     *
     * @param context  the context
     * @param articles articles to represent in the ListView
     */
    public ArticleAdapter(Context context, ArrayList<TopThemaArticle> articles) {
        super(context, 0, articles);
    }

    /**
     * Sets list item text views.
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView titleTextView = (TextView) view.findViewById(R.id.titleTV);
        TextView descriptionView = (TextView) view.findViewById(R.id.descriptionTV);

        titleTextView.setText(getItem(position).getTitle());
        descriptionView.setText(getItem(position).getDescription());
        return view;

    }
}
