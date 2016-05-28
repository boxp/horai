package tk.boxp.horai;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class TweetListFragment extends ListFragment {

    private Twitter mTwitter;

    private CardViewAdapter mCardViewAdapter;

    private Tweet mTweet;

    private List<Status> mStatuses = null;

    private CompositeSubscription mSubscriptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mStatuses = new ArrayList<>();
        mTweet = new Tweet(getActivity());

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        //OAuthの設定
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.setOAuthConsumerKey(getString(R.string.twitter_consumerkey));
        configurationBuilder.setOAuthConsumerSecret(getString(R.string.twitter_consumersecret));
        configurationBuilder.setOAuthAccessToken(getString(R.string.twitter_accesstoken));
        configurationBuilder.setOAuthAccessTokenSecret(getString(R.string.twitter_accesstokensecret));

        Configuration conf = configurationBuilder.build();

        // Twitterオブジェクトの生成
        mTwitter = new TwitterFactory(conf).getInstance();
        //TODO OAuth

        return view;
    }

    Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCardViewAdapter = new CardViewAdapter(getActivity(), mStatuses);

        mSubscriptions.add(Observable.create(mTweet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Status>() {
                    @Override
                    public void call(Status status) {
                        mStatuses.add(status);
                        mCardViewAdapter.notifyDataSetChanged();
                    }
                }));
        mSubscriptions.unsubscribe();
        setListAdapter(mCardViewAdapter);
    }

}
