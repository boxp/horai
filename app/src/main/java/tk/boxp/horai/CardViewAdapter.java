package tk.boxp.horai;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import twitter4j.Status;

/**
 * @author koichi kukino
 *         date  16/05/28
 */
public class CardViewAdapter extends ArrayAdapter<Status> {

    private Context mContext;

    public CardViewAdapter(Context context, List<Status> list) {
        super(context, 0, list);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tweet_card, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Status status = getItem(position);
        final String text = status.getUser().getName() + ":" + status.getText();
        holder.textBox.setText(text);
        return convertView;
    }

    class ViewHolder {

        CardView cardView;

        TextView textBox;

        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.cardView);
            textBox = (TextView) view.findViewById(R.id.textBox);
        }
    }
}