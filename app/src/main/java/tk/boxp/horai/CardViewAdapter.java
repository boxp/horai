package tk.boxp.horai;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.picasso.Picasso;

import java.util.List;

import twitter4j.ExtendedMediaEntity;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;

/**
 * @author koichi kukino
 *         date  16/05/28
 */
public class CardViewAdapter extends ArrayAdapter<Status> {

    List<Status> list;

    public CardViewAdapter(Context context, List<Status> list) {
        super(context, 0, list);
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Context ctx = getContext();

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_tweet_card, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Status status = getItem(position);
        final User user = status.getUser();
        final String profileImgUrl = user.getBiggerProfileImageURL();
        final String name = "@" + user.getScreenName();
        final String date = status.getCreatedAt().toString();
        final String text = status.getText();
        final ExtendedMediaEntity[] medias = status.getExtendedMediaEntities();

        // プロファイル画像の設定
        Picasso.with(ctx).load(profileImgUrl).into(holder.profileImg);
        this.setTweetImage(ctx, medias, holder);
        holder.textBox.setText(text);
        holder.nameBox.setText(name);
        holder.dateBox.setText(date);

        return convertView;
    }

    // tweetImg にメディアを設定するサブルーチン
    private void setTweetImage(Context ctx, ExtendedMediaEntity[] medias, ViewHolder holder) {
        if (medias.length > 0) {
            ExtendedMediaEntity media = medias[0];

            Log.d(CardViewAdapter.class.getSimpleName(), media.getType());

            switch (media.getType()) {
                case "photo":
                    String mediaURL = media.getMediaURLHttps();

                    holder.tweetImg.setVisibility(View.VISIBLE);
                    holder.tweetVideo.setVisibility(View.GONE);
                    Picasso.with(getContext()).load(mediaURL).into(holder.tweetImg);
                    Log.d(CardViewAdapter.class.getSimpleName(), mediaURL);
                    break;
                case "video":
                case "animated_gif":
                    String videoURL = media.getVideoVariants()[0].getUrl();

                    holder.tweetImg.setVisibility(View.GONE);
                    holder.tweetVideo.setVisibility(View.VISIBLE);
                    holder.tweetVideo.setVideoURI(Uri.parse(videoURL));
                    holder.tweetVideo.start();
                    Log.d(CardViewAdapter.class.getSimpleName(), videoURL);
                    Log.d(CardViewAdapter.class.getSimpleName(), String.valueOf(holder.tweetVideo.isPlaying()));
                    break;
            }
        } else {
            // tweetImg, tweetVideo を非表示に
            holder.tweetImg.setVisibility(View.GONE);
            holder.tweetVideo.setVisibility(View.GONE);
        }
    }

    //

    class ViewHolder {

        CardView cardView;

        TextView textBox;
        ImageView profileImg;
        TextView nameBox;
        TextView dateBox;
        ImageView tweetImg;
        VideoView tweetVideo;


        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.cardView);
            textBox = (TextView) view.findViewById(R.id.textBox);
            profileImg = (ImageView) view.findViewById(R.id.profileImg);
            nameBox = (TextView) view.findViewById(R.id.nameBox);
            dateBox = (TextView) view.findViewById(R.id.dateBox);
            tweetImg = (ImageView) view.findViewById(R.id.tweetImg);
            tweetVideo = (VideoView) view.findViewById(R.id.tweetVideo);
        }
    }
}