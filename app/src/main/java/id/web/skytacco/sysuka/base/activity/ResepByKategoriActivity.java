package id.web.skytacco.sysuka.base.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.adapter.ResepbykategoriAdapter;
import id.web.skytacco.sysuka.entity.ResepItem;
import id.web.skytacco.sysuka.network.JsonNetwork;
import id.web.skytacco.sysuka.util.DesainTampilan;
import id.web.skytacco.sysuka.util.Utils;

public class ResepByKategoriActivity extends AppCompatActivity {
    static final String TAG = "RecipesByCategory";
    RecyclerView recyclerView;
    List<ResepItem> arrayItemRecipesList;
    ResepbykategoriAdapter adapterRecipesByCategory;
    ArrayList<String> array_news, array_news_cat_name, array_cid, array_cat_id, array_cat_image, array_cat_name, array_title, array_image, array_desc, array_date;
    String[] str_news, str_news_cat_name;
    String[] str_cid, str_cat_id, str_cat_image, str_cat_name, str_title, str_image, str_desc, str_date;
    JsonNetwork jsonUtils;
    int textLength = 0;
    SwipeRefreshLayout mSwipeRefreshLayout = null;
    private ResepItem itemRecipesList;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep_by_kategori);
        relativeLayout = findViewById(R.id.no_network);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), Utils.NUM_OF_COLUMNS));
        DesainTampilan itemDecoration = new DesainTampilan(getApplicationContext(), R.dimen.desain_tampilan);
        recyclerView.addItemDecoration(itemDecoration);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Utils.CATEGORY_TITLE);
        }

        arrayItemRecipesList = new ArrayList<ResepItem>();
        array_news = new ArrayList<>();
        array_news_cat_name = new ArrayList<>();
        array_cid = new ArrayList<>();
        array_cat_id = new ArrayList<>();
        array_cat_image = new ArrayList<>();
        array_cat_name = new ArrayList<>();
        array_title = new ArrayList<>();
        array_image = new ArrayList<>();
        array_desc = new ArrayList<>();
        array_date = new ArrayList<>();

        str_news = new String[array_news.size()];
        str_news_cat_name = new String[array_news_cat_name.size()];
        str_cid = new String[array_cid.size()];
        str_cat_id = new String[array_cat_id.size()];
        str_cat_image = new String[array_cat_image.size()];
        str_cat_name = new String[array_cat_name.size()];
        str_title = new String[array_title.size()];
        str_image = new String[array_image.size()];
        str_desc = new String[array_desc.size()];
        str_date = new String[array_date.size()];

        jsonUtils = new JsonNetwork(getApplicationContext());

        if (JsonNetwork.isNetworkAvailable(ResepByKategoriActivity.this)) {
            new MyTask().execute(Utils.SERVER_URL + "/api.php?cat_id=" + Utils.CATEGORY_IDD);
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
            relativeLayout.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array_news.clear();
                array_news_cat_name.clear();
                array_cid.clear();
                array_cat_id.clear();
                array_cat_image.clear();
                array_cat_name.clear();
                array_title.clear();
                array_image.clear();
                array_desc.clear();
                array_date.clear();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        clearData();
                        new MyTask().execute(Utils.SERVER_URL + "/api.php?cat_id=" + Utils.CATEGORY_IDD);
                    }
                }, 1500);
            }
        });
    }

    public void clearData() {
        int size = this.arrayItemRecipesList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.arrayItemRecipesList.remove(0);
            }

            adapterRecipesByCategory.notifyItemRangeRemoved(0, size);
        }
    }

    public void setAdapterToRecyclerView() {
        adapterRecipesByCategory = new ResepbykategoriAdapter(ResepByKategoriActivity.this, arrayItemRecipesList);
        recyclerView.setAdapter(adapterRecipesByCategory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);


        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView)
                MenuItemCompat.getActionView(menu.findItem(R.id.search));

        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                textLength = newText.length();
                arrayItemRecipesList.clear();

                for (int i = 0; i < str_title.length; i++) {
                    if (textLength <= str_title[i].length()) {
                        if (str_title[i].toLowerCase().contains(newText.toLowerCase())) {
                            ResepItem objItem = new ResepItem();
                            objItem.setCategoryName(str_cat_name[i]);
                            objItem.setCatId(str_cat_id[i]);
                            objItem.setCId(str_cid[i]);
                            objItem.setNewsDate(str_date[i]);
                            objItem.setNewsDescription(str_desc[i]);
                            objItem.setNewsHeading(str_title[i]);
                            objItem.setNewsImage(str_image[i]);
                            arrayItemRecipesList.add(objItem);
                        }
                    }
                }
                setAdapterToRecyclerView();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonNetwork.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            mSwipeRefreshLayout.setRefreshing(false);

            if (null == result || result.length() == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.VISIBLE);
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Utils.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ResepItem objItem = new ResepItem();

                        objItem.setCId(objJson.getString(Utils.CATEGORY_ITEM_CID));
                        objItem.setCategoryName(objJson.getString(Utils.CATEGORY_ITEM_NAME));
                        objItem.setCategoryImage(objJson.getString(Utils.CATEGORY_ITEM_IMAGE));
                        objItem.setCatId(objJson.getString(Utils.CATEGORY_ITEM_CAT_ID));
                        objItem.setNewsImage(objJson.getString(Utils.CATEGORY_ITEM_NEWSIMAGE));
                        objItem.setNewsHeading(objJson.getString(Utils.CATEGORY_ITEM_NEWSHEADING));
                        objItem.setNewsDescription(objJson.getString(Utils.CATEGORY_ITEM_NEWSDESCRI));
                        objItem.setNewsDate(objJson.getString(Utils.CATEGORY_ITEM_NEWSDATE));

                        arrayItemRecipesList.add(objItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < arrayItemRecipesList.size(); j++) {
                    itemRecipesList = arrayItemRecipesList.get(j);
                    array_cat_id.add(itemRecipesList.getCatId());
                    array_cat_name.add(itemRecipesList.getCategoryName());
                    array_cid.add(String.valueOf(itemRecipesList.getCId()));
                    array_image.add(String.valueOf(itemRecipesList.getNewsImage()));
                    array_title.add(String.valueOf(itemRecipesList.getNewsHeading()));
                    array_desc.add(String.valueOf(itemRecipesList.getNewsDescription()));
                    array_date.add(String.valueOf(itemRecipesList.getNewsDate()));

                    str_cat_id = array_cat_id.toArray(str_cat_id);
                    str_cat_name = array_cat_name.toArray(str_cat_name);
                    str_cid = array_cid.toArray(str_cid);
                    str_image = array_image.toArray(str_image);
                    str_title = array_title.toArray(str_title);
                    str_desc = array_desc.toArray(str_desc);
                    str_date = array_date.toArray(str_date);
                }
                setAdapterToRecyclerView();
            }
        }
    }


}
