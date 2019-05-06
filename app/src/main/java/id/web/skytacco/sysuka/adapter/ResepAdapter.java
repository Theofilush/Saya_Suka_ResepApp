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
import id.web.skytacco.sysuka.base.activity.DetailActivity;
import id.web.skytacco.sysuka.entity.ResepItem;
import id.web.skytacco.sysuka.util.Utils;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.ViewHolder> {
    private Context context;
    private List<ResepItem> mListResepItem;
    private ResepItem itemRecipesList;
    private int counter = 1;

    public ResepAdapter(Context context, List<ResepItem> abc) {
        this.context = context;
        this.mListResepItem = abc;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resep, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResepAdapter.ViewHolder holder, final int position) {
        itemRecipesList = mListResepItem.get(position);

        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.title.setTypeface(font1);

        holder.title.setText(itemRecipesList.getNewsHeading());

        Picasso.get().load(Utils.SERVER_URL + "/upload/thumbs/" +
                itemRecipesList.getNewsImage()).placeholder(R.drawable.ic_spatula_svgrepo).into(holder.image);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemRecipesList = mListResepItem.get(position);

                int pos = Integer.parseInt(itemRecipesList.getCatId());

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("POSITION", pos);
                Utils.NEWS_ITEMID = itemRecipesList.getCatId();

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListResepItem.size();
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
