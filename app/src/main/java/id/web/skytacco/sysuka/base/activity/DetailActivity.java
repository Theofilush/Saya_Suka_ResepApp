package id.web.skytacco.sysuka.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.web.skytacco.sysuka.R;

public class DetailActivity extends AppCompatActivity {
    static final String TAG = "RecipesDetail";
    final Context context = this;
    String str_cid, str_cat_id, str_cat_image, str_cat_name, str_title, str_image, str_desc, str_date;
    TextView news_title, news_date;
    WebView news_desc;
    ImageView img_news, img_fav;
    DatabaseHandler db;
    List<ItemRecipesList> arrayItemRecipesList;
    ItemRecipesList itemRecipesList;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ProgressBar progressBar;
    CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(itemRecipesList.getCategoryName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        coordinatorLayout = findViewById(R.id.main_content);
        progressBar = findViewById(R.id.progressBar);

        img_news = findViewById(R.id.image);
        img_fav = (FloatingActionButton) findViewById(R.id.img_fav);

        news_title = findViewById(R.id.title);
        news_date = findViewById(R.id.date);
        news_desc = findViewById(R.id.desc);

        db = new DatabaseHandler(ActivityRecipesDetail.this);

        arrayItemRecipesList = new ArrayList<ItemRecipesList>();

        if (JsonUtils.isNetworkAvailable(ActivityRecipesDetail.this)) {
            new MyTask().execute(Config.SERVER_URL + "/api.php?nid=" + JsonConfig.NEWS_ITEMID);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
        }

    }

    public void setAdapterToRecyclerView() {

        if (Config.ENABLE_RTL_MODE) {

            itemRecipesList = arrayItemRecipesList.get(0);
            str_cid = itemRecipesList.getCId();
            str_cat_name = itemRecipesList.getCategoryName();
            str_cat_image = itemRecipesList.getCategoryImage();
            str_cat_id = itemRecipesList.getCatId();
            str_title = itemRecipesList.getNewsHeading();
            str_desc = itemRecipesList.getNewsDescription();
            str_image = itemRecipesList.getNewsImage();
            str_date = itemRecipesList.getNewsDate();

            news_title.setText(str_title);
            news_date.setText(str_date);

            news_desc.setBackgroundColor(Color.parseColor("#ffffff"));
            news_desc.setFocusableInTouchMode(false);
            news_desc.setFocusable(false);
            news_desc.getSettings().setDefaultTextEncodingName("UTF-8");

            WebSettings webSettings = news_desc.getSettings();
            Resources res = getResources();
            int fontSize = res.getInteger(R.integer.font_size);
            webSettings.setDefaultFontSize(fontSize);

            String mimeType = "text/html; charset=UTF-8";
            String encoding = "utf-8";
            String htmlText = str_desc;

            String text = "<html dir='rtl'><head>"
                    + "<style type=\"text/css\">body{color: #525252;}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            news_desc.loadData(text, mimeType, encoding);

            Picasso
                    .get()
                    .load(Config.SERVER_URL + "/upload/" + itemRecipesList.getNewsImage())
                    .placeholder(R.drawable.ic_thumbnail)
                    .into(img_news);

            List<ItemFavorite> itemFavorites = db.getFavRow(str_cat_id);
            if (itemFavorites.size() == 0) {
                img_fav.setImageResource(R.drawable.ic_favorite_outline_white);
            } else {
                if (itemFavorites.get(0).getCatId().equals(str_cat_id)) {
                    img_fav.setImageResource(R.drawable.ic_favorite_white);
                }
            }

            img_fav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    List<ItemFavorite> itemFavorites = db.getFavRow(str_cat_id);
                    if (itemFavorites.size() == 0) {

                        db.AddtoFavorite(new ItemFavorite(str_cat_id, str_cid, str_cat_name, str_title, str_image, str_desc, str_date));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
                        img_fav.setImageResource(R.drawable.ic_favorite_white);

                    } else {
                        if (itemFavorites.get(0).getCatId().equals(str_cat_id)) {

                            db.RemoveFav(new ItemFavorite(str_cat_id));
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
                            img_fav.setImageResource(R.drawable.ic_favorite_outline_white);
                        }
                    }
                }
            });

        } else {

            itemRecipesList = arrayItemRecipesList.get(0);
            str_cid = itemRecipesList.getCId();
            str_cat_name = itemRecipesList.getCategoryName();
            str_cat_image = itemRecipesList.getCategoryImage();
            str_cat_id = itemRecipesList.getCatId();
            str_title = itemRecipesList.getNewsHeading();
            str_desc = itemRecipesList.getNewsDescription();
            str_image = itemRecipesList.getNewsImage();
            str_date = itemRecipesList.getNewsDate();

            news_title.setText(str_title);
            news_date.setText(str_date);

            news_desc.setBackgroundColor(Color.parseColor("#ffffff"));
            news_desc.setFocusableInTouchMode(false);
            news_desc.setFocusable(false);
            news_desc.getSettings().setDefaultTextEncodingName("UTF-8");

            WebSettings webSettings = news_desc.getSettings();
            Resources res = getResources();
            int fontSize = res.getInteger(R.integer.font_size);
            webSettings.setDefaultFontSize(fontSize);

            String mimeType = "text/html; charset=UTF-8";
            String encoding = "utf-8";
            String htmlText = str_desc;

            String text = "<html><head>"
                    + "<style type=\"text/css\">body{color: #525252;}"
                    + "</style></head>"
                    + "<body>"
                    + htmlText
                    + "</body></html>";

            news_desc.loadData(text, mimeType, encoding);

            Picasso
                    .get()
                    .load(Config.SERVER_URL + "/upload/" + itemRecipesList.getNewsImage())
                    .placeholder(R.drawable.ic_thumbnail)
                    .into(img_news);

            List<ItemFavorite> itemFavorites = db.getFavRow(str_cat_id);
            if (itemFavorites.size() == 0) {
                img_fav.setImageResource(R.drawable.ic_favorite_outline_white);
            } else {
                if (itemFavorites.get(0).getCatId().equals(str_cat_id)) {
                    img_fav.setImageResource(R.drawable.ic_favorite_white);
                }
            }

            img_fav.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    List<ItemFavorite> itemFavorites = db.getFavRow(str_cat_id);
                    if (itemFavorites.size() == 0) {

                        db.AddtoFavorite(new ItemFavorite(str_cat_id, str_cid, str_cat_name, str_title, str_image, str_desc, str_date));
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.favorite_added), Toast.LENGTH_SHORT).show();
                        img_fav.setImageResource(R.drawable.ic_favorite_white);

                    } else {
                        if (itemFavorites.get(0).getCatId().equals(str_cat_id)) {

                            db.RemoveFav(new ItemFavorite(str_cat_id));
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show();
                            img_fav.setImageResource(R.drawable.ic_favorite_outline_white);
                        }
                    }
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_share:

                String formattedString = android.text.Html.fromHtml(str_desc).toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, str_title + "\n" + formattedString + "\n" + getResources().getString(R.string.share_text) + "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);

            if (null == result || result.length() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
                coordinatorLayout.setVisibility(View.GONE);
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ItemRecipesList objItem = new ItemRecipesList();

                        objItem.setCId(objJson.getString(JsonConfig.CATEGORY_ITEM_CID));
                        objItem.setCategoryName(objJson.getString(JsonConfig.CATEGORY_ITEM_NAME));
                        objItem.setCategoryImage(objJson.getString(JsonConfig.CATEGORY_ITEM_IMAGE));
                        objItem.setCatId(objJson.getString(JsonConfig.CATEGORY_ITEM_CAT_ID));
                        objItem.setNewsImage(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSIMAGE));
                        objItem.setNewsHeading(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSHEADING));
                        objItem.setNewsDescription(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSDESCRI));
                        objItem.setNewsDate(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSDATE));

                        arrayItemRecipesList.add(objItem);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setAdapterToRecyclerView();
            }

        }
    }

}
