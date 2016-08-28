package tk.boxp.horai;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        final String name = user.getScreenName();
        final String date = status.getCreatedAt().toString();
        final String text = status.getText();
        final MediaEntity[] medias = status.getMediaEntities();

        // プロファイル画像の設定
        Picasso.with(ctx).load(profileImgUrl).into(holder.profileImg);
        this.setTweetImage(ctx, medias, holder);
        holder.textBox.setText(text);
        holder.nameBox.setText(name);
        holder.dateBox.setText(date);

        return convertView;
    }

    // tweetImgにメディアを設定するサブルーチン
    private void setTweetImage(Context ctx, MediaEntity[] medias, ViewHolder holder) {
        if (medias.length > 0) {
            MediaEntity media = medias[0];

            holder.tweetImg.setVisibility(View.VISIBLE);

            switch (media.getType()) {
                case "photo":
                    Picasso.with(getContext()).load(media.getMediaURL()).into(holder.tweetImg);
                    break;
                case "animated_gif":
                    GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(holder.tweetImg);
                    Glide.with(ctx).load(media.getMediaURL()).into(target);
                    break;
            }
        } else {
            // tweetImgを非表示に
            holder.tweetImg.setVisibility(View.GONE);
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

        public ViewHolder(View view) {
            cardView = (CardView) view.findViewById(R.id.cardView);
            textBox = (TextView) view.findViewById(R.id.textBox);
            profileImg = (ImageView) view.findViewById(R.id.profileImg);
            nameBox = (TextView) view.findViewById(R.id.nameBox);
            dateBox = (TextView) view.findViewById(R.id.dateBox);
            tweetImg = (ImageView) view.findViewById(R.id.tweetImg);
        }
    }
}