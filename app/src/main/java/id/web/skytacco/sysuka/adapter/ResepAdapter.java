package id.web.skytacco.sysuka.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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
import id.web.skytacco.sysuka.entity.ResepItem;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ViewHolder> {
    private Context context;
    private List<ResepItem> arrayItemRecipesList;
    private ResepItem itemRecipesList;
    private int counter = 1;

    public ResepAdapter(Context context, List<ResepItem> arrayItemRecipesList) {
        this.context = context;
        this.arrayItemRecipesList = arrayItemRecipesList;
    }

    @NonNull
    @Override
    public ResepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lsv_item_recipes_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lsv_item_recipes_list, parent, false);

        loadInterstitialAd();

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ResepAdapter.ViewHolder holder, int position) {
        itemRecipesList = arrayItemRecipesList.get(position);

        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.title.setTypeface(font1);

        holder.title.setText(itemRecipesList.getNewsHeading());

        Picasso.get().load(Config.SERVER_URL + "/upload/thumbs/" +
                itemRecipesList.getNewsImage()).placeholder(R.drawable.ic_thumbnail).into(holder.image);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemRecipesList = arrayItemRecipesList.get(position);

                int pos = Integer.parseInt(itemRecipesList.getCatId());

                Intent intent = new Intent(context, ActivityRecipesDetail.class);
                intent.putExtra("POSITION", pos);
                JsonConfig.NEWS_ITEMID = itemRecipesList.getCatId();

                context.startActivity(intent);

                showInterstitialAd();

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayItemRecipesList.size();
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
