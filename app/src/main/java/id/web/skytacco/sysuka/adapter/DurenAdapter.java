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
import id.web.skytacco.sysuka.entity.DurenItem;
import id.web.skytacco.sysuka.util.Utils;

public class DurenAdapter extends RecyclerView.Adapter<DurenAdapter.ViewHolder> {
    private Context context;
    private List<DurenItem> mListDurenItem;
    private DurenItem mDurenItem;

    public DurenAdapter(Context context, List<DurenItem> abc) {
        this.context = context;
        this.mListDurenItem = abc;
    }

    @NonNull
    @Override
    public DurenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_resep, parent, false);
        return new DurenAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DurenAdapter.ViewHolder holder, final int position) {
        mDurenItem = mListDurenItem.get(position);

        Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.title.setTypeface(font1);

        holder.title.setText(mDurenItem.getNewsHeading());

        Picasso.get()
                .load(Utils.SERVER_URL + "/upload/thumbs/" + mDurenItem.getNewsImage())
                .placeholder(R.drawable.ic_spatula_svgrepo)
                .into(holder.image);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDurenItem = mListDurenItem.get(position);

                int pos = Integer.parseInt(mDurenItem.getCatId());

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("POSITION", pos);
                Utils.NEWS_ITEMID = mDurenItem.getCatId();

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListDurenItem.size();
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
