package id.web.skytacco.sysuka.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.base.activity.DetailActivity;
import id.web.skytacco.sysuka.entity.FavoriteItem;
import id.web.skytacco.sysuka.util.Utils;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    FavoriteItem itemFavorite;
    private Context context;
    private List<FavoriteItem> arrayItemFavorite;

    public FavoriteAdapter(Context mContext, List<FavoriteItem> arrayItemFavorite) {
        this.context = mContext;
        this.arrayItemFavorite = arrayItemFavorite;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resep, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        itemFavorite = arrayItemFavorite.get(position);

        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.title.setTypeface(font1);

        holder.title.setText(itemFavorite.getNewsHeading());

        Picasso.get()
                .load(Utils.SERVER_URL + "/upload/thumbs/" + itemFavorite.getNewsImage())
                .placeholder(R.drawable.ic_spatula_svgrepo)
                .into(holder.image);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                itemFavorite = arrayItemFavorite.get(position);
                int pos = Integer.parseInt(itemFavorite.getCatId());

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("POSITION", pos);
                Utils.NEWS_ITEMID = itemFavorite.getCatId();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayItemFavorite.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.news_title);
            image = view.findViewById(R.id.news_image);
            relativeLayout = view.findViewById(R.id.relativeLayout);
        }
    }
}
