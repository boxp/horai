package tk.boxp.horai;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by boxp on 16/05/14.
 */
public class TweetListFragment extends Fragment {

    private Twitter mTwitter;

    private CardViewAdapter mCardViewAdapter;

    private Tweet mTweet;

    private ArrayList<Status> mStatuses = null;

    private CompositeSubscription mSubscriptions = new CompositeSubscription();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //OAuthの設定
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setOAuthConsumerKey(getString(R.string.twitter_consumerkey));
        configurationBuilder.setOAuthConsumerSecret(getString(R.string.twitter_consumersecret));
        configurationBuilder.setOAuthAccessToken(getString(R.string.twitter_accesstoken));
        configurationBuilder.setOAuthAccessTokenSecret(getString(R.string.twitter_accesstokensecret));

        Configuration conf = configurationBuilder.build();

        // Twitterオブジェクトの生成
        this.mTwitter = new TwitterFactory(conf).getInstance();
        //TODO OAuth

        mStatuses = new ArrayList<>();
        mTweet = new Tweet(getActivity());

        this.mCardViewAdapter = new CardViewAdapter(getActivity(), mStatuses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView)view.findViewById(R.id.tweet_list);

        listView.setAdapter(mCardViewAdapter);
        this.mSubscriptions.add(Observable.create(mTweet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Status>() {
                    @Override
                    public void call(Status status) {
                        Log.d(TweetListFragment.class.getSimpleName(), status.getURLEntities().toString());
                        mStatuses.add(0, status);
                        mCardViewAdapter.notifyDataSetChanged();
                    }
                }));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
