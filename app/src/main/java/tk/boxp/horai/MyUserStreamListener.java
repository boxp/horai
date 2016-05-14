package tk.boxp.horai;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import twitter4j.Status;
import twitter4j.UserStreamAdapter;

/**
 * Created by boxp on 16/05/14.
 */
public class MyUserStreamListener extends UserStreamAdapter {

    private static Handler handler = new Handler();
    private int id = 0;

    private View view = null;
    private LayoutInflater inflater = null;

    private LinearLayout cardLinear = null;

    public MyUserStreamListener(View view, LayoutInflater inflater) {
        this.view = view;
        this.cardLinear = (LinearLayout)view.findViewById(R.id.cardLinear);
        this.inflater = inflater;

    }

    @Override
    public void onStatus(Status status) {
        final String text = status.getUser().getName() + ":" + status.getText();

        this.handler.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.tweet_card, null);
                CardView cardView = (CardView) linearLayout.findViewById(R.id.cardView);
                TextView textBox = (TextView) linearLayout.findViewById(R.id.textBox);

                textBox.setText(text);
                cardView.setTag(id);
                cardLinear.addView(linearLayout, id);

                id++;
            }
        });
    }
}
