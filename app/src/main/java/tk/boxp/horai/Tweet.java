package tk.boxp.horai;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

/**
 * @author koichi kukino
 *         date  16/05/28
 */
public class Tweet implements Observable.OnSubscribe<Status>{

    private TwitterStream mTwitterStream;
    private Subscriber<? super Status> mSubscriber;

    public Tweet(Context context) {
        String consumerKey = context.getString(R.string.twitter_consumerkey);
        String consumerSecret = context.getString(R.string.twitter_consumersecret);
        mTwitterStream = TwitterStreamFactory.getSingleton();
        mTwitterStream.setOAuthConsumer(consumerKey, consumerSecret);
        mTwitterStream.setOAuthAccessToken(getAccessToken(context));
    }

    @Override
    public void call(Subscriber<? super Status> subscriber) {
        mSubscriber = subscriber;
        mSubscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                mTwitterStream.clearListeners();
                mTwitterStream.shutdown();
                mTwitterStream = null;
            }
        }));
        mTwitterStream.addListener(new TweetListener());
        mTwitterStream.user();
    }

    public class TweetListener implements StatusListener {
        @Override
        public void onStatus(Status status) {
            mSubscriber.onNext(status);
        }

        @Override
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

        }

        @Override
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

        }

        @Override
        public void onScrubGeo(long userId, long upToStatusId) {

        }

        @Override
        public void onStallWarning(StallWarning warning) {

        }

        @Override
        public void onException(Exception ex) {

        }
    }

    public AccessToken getAccessToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        //String token = sp.getString(context.getString(R.string.twitter_accesstoken), null);
        //String secret = sp.getString(context.getString(R.string.twitter_accesstokensecret), null);
        String token = context.getString(R.string.twitter_accesstoken);
        String secret = context.getString(R.string.twitter_accesstokensecret);

        if (token != null && secret != null) {
            return new AccessToken(token, secret);
        } else {
            return null;
        }
    }

}
