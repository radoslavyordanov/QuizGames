package radoslav.yordanov.quizgames.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import radoslav.yordanov.quizgames.model.Result;
import radoslav.yordanov.quizgames.R;

public class TopScoresAdapter extends ArrayAdapter<Result> {
    private ArrayList<Result> resultsList;
    private LayoutInflater vi;
    private int Resource;
    private ViewHolder holder;

    public TopScoresAdapter(Context context, int resource, ArrayList<Result> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        resultsList = objects;
    }

    /**
     * Get a View that displays the data at the specified position in the data set.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view is non-null and of an appropriate type before using.
     * @param parent      The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.name = (TextView) v.findViewById(R.id.name);
            holder.score = (TextView) v.findViewById(R.id.score);
            holder.date = (TextView) v.findViewById(R.id.date);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.name.setText(resultsList.get(position).getName());
        holder.score.setText(String.valueOf(resultsList.get(position).getScore()));
        holder.date.setText(resultsList.get(position).getDate());

        return v;

    }

    /**
     * Defines movie list row elements.
     */
    static class ViewHolder {
        public TextView name;
        public TextView score;
        public TextView date;
    }

}
