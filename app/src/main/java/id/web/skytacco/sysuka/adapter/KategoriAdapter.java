package id.web.skytacco.sysuka.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.base.activity.ResepByKategoriActivity;
import id.web.skytacco.sysuka.entity.KategoriItem;
import id.web.skytacco.sysuka.util.Utils;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.ViewHolder> {
    KategoriItem mKategoriItem;
    private Context context;
    private List<KategoriItem> arrayItemCategory;

    public KategoriAdapter(Context mContext, List<KategoriItem> arrayItemCategory) {
        this.context = mContext;
        this.arrayItemCategory = arrayItemCategory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kategori, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mKategoriItem = arrayItemCategory.get(position);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        holder.title.setTypeface(font);
        holder.title.setText(mKategoriItem.getCategoryName());

        Picasso.get()
                .load(Utils.SERVER_URL + "/upload/category/" + mKategoriItem.getCategoryImageurl())
                .placeholder(R.drawable.ic_spatula_svgrepo)
                .into(holder.image);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mKategoriItem = arrayItemCategory.get(position);
                int catId = mKategoriItem.getCategoryId();
                Utils.CATEGORY_IDD = mKategoriItem.getCategoryId();
                Log.e("cat_id", "" + catId);
                Utils.CATEGORY_TITLE = mKategoriItem.getCategoryName();

                Intent intent = new Intent(context, ResepByKategoriActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayItemCategory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.category_title);
            image = view.findViewById(R.id.category_image);
            relativeLayout = view.findViewById(R.id.relativeLayout);
        }
    }
}
