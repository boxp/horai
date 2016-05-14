package tk.boxp.horai;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by boxp on 16/05/14.
 */
public class PlaceholderFragment extends Fragment {
    private Twitter twitter = null;
    private TwitterStream twitterStream = null;
    private List<Status> statuses = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // cardLinearをリセット
        LinearLayout cardLinear = (LinearLayout)view.findViewById(R.id.cardLinear);
        cardLinear.removeAllViews();

        //OAuthの設定
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setOAuthConsumerKey(getString(R.string.twitter_consumerkey));
        configurationBuilder.setOAuthConsumerSecret(getString(R.string.twitter_consumersecret));
        configurationBuilder.setOAuthAccessToken(getString(R.string.twitter_accesstoken));
        configurationBuilder.setOAuthAccessTokenSecret(getString(R.string.twitter_accesstokensecret));

        Configuration conf = configurationBuilder.build();

        // Twitterオブジェクトの生成
        this.twitter = new TwitterFactory(conf).getInstance();
        this.twitterStream = new TwitterStreamFactory(conf).getInstance();

        this.twitterStream.addListener(new MyUserStreamListener(view, inflater));
        this.twitterStream.user();

        return view;

    }

}
