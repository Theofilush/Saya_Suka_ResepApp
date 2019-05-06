package id.web.skytacco.sysuka.base.fragment;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.adapter.ResepAdapter;
import id.web.skytacco.sysuka.entity.ResepItem;
import id.web.skytacco.sysuka.network.JsonNetwork;
import id.web.skytacco.sysuka.util.Utils;

public class ResepFragment extends Fragment {

    public ResepFragment() {
    }

    RecyclerView recyclerView;
    List<ResepItem> arrayResepItem;
    ResepAdapter mResepAdapter;
    ArrayList<String> array_news, array_news_cat_name, array_cid, array_cat_id, array_cat_name, array_title, array_image, array_desc, array_date;
    String[] str_news, str_news_cat_name, str_cid, str_cat_id, str_cat_name, str_title, str_image, str_desc, str_date;
    JsonNetwork mJsonNetwork;
    int textLength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    private ResepItem mResepItem;
    private RelativeLayout rootLayout;
    private RelativeLayout relativeLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_resep, container, false);
        setHasOptionsMenu(true);

        recyclerView = v.findViewById(R.id.recycler_view);

        rootLayout = v.findViewById(R.id.rootLayout);

        relativeLayout = v.findViewById(R.id.no_network);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Utils.NUM_OF_COLUMNS));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        arrayResepItem = new ArrayList<ResepItem>();
        array_news = new ArrayList<String>();
        array_news_cat_name = new ArrayList<String>();
        array_cid = new ArrayList<String>();
        array_cat_id = new ArrayList<String>();
        array_cat_name = new ArrayList<String>();
        array_title = new ArrayList<String>();
        array_image = new ArrayList<String>();
        array_desc = new ArrayList<String>();
        array_date = new ArrayList<String>();

        str_news = new String[array_news.size()];
        str_news_cat_name = new String[array_news_cat_name.size()];
        str_cid = new String[array_cid.size()];
        str_cat_id = new String[array_cat_id.size()];
        str_cat_name = new String[array_cat_name.size()];
        str_title = new String[array_title.size()];
        str_image = new String[array_image.size()];
        str_desc = new String[array_desc.size()];
        str_date = new String[array_date.size()];

        mJsonNetwork = new JsonNetwork(getActivity());

        if (JsonNetwork.isNetworkAvailable(getActivity())) {
            new MyTask().execute(Utils.SERVER_URL + "/api.php?latest_news=" + Utils.NUM_OF_RECENT_RECIPES);
        } else {
            Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!!", Toast.LENGTH_SHORT).show();
            relativeLayout.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                array_news.clear();
                array_news_cat_name.clear();
                array_cid.clear();
                array_cat_id.clear();
                array_cat_name.clear();
                array_title.clear();
                array_image.clear();
                array_desc.clear();
                array_date.clear();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        clearData();
                        new MyTask().execute(Config.SERVER_URL + "/api.php?latest_news=50");
                    }
                }, 1500);
            }
        });

        return v;
    }

    public void clearData() {
        int size = this.arrayResepItem.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.arrayResepItem.remove(0);
            }

            mResepAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    public void setAdapterToRecyclerView() {
        mResepAdapter = new ResepAdapter(getActivity(), arrayResepItem);
        recyclerView.setAdapter(mResepAdapter);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
                arrayResepItem.clear();

                for (int i = 0; i < str_title.length; i++) {
                    if (textLength <= str_title[i].length()) {
                        if (str_title[i].toLowerCase().contains(newText.toLowerCase())) {

                            ResepItem objItem = new ResepItem();

                            objItem.setCategoryName((str_cat_name[i]));
                            objItem.setCatId(str_cat_id[i]);
                            objItem.setCId(str_cid[i]);
                            objItem.setNewsDate(str_date[i]);
                            objItem.setNewsDescription(str_desc[i]);
                            objItem.setNewsHeading(str_title[i]);
                            objItem.setNewsImage(str_image[i]);
                            arrayResepItem.add(objItem);
                        }
                    }
                }

                setAdapterToRecyclerView();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:

                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonNetwork.getJSONString(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            swipeRefreshLayout.setRefreshing(false);

            if (null == result || result.length() == 0) {
                Toast.makeText(getActivity(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.VISIBLE);
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(JsonConfig.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);

                        ResepItem objItem = new ResepItem();

                        objItem.setCId(objJson.getString(JsonConfig.CATEGORY_ITEM_CID));
                        objItem.setCategoryName(objJson.getString(JsonConfig.CATEGORY_ITEM_NAME));
                        objItem.setCatId(objJson.getString(JsonConfig.CATEGORY_ITEM_CAT_ID));
                        objItem.setNewsImage(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSIMAGE));
                        objItem.setNewsHeading(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSHEADING));
                        objItem.setNewsDescription(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSDESCRI));
                        objItem.setNewsDate(objJson.getString(JsonConfig.CATEGORY_ITEM_NEWSDATE));

                        arrayResepItem.add(objItem);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < arrayResepItem.size(); j++) {

                    mResepItem = arrayResepItem.get(j);

                    array_cat_id.add(mResepItem.getCatId());
                    str_cat_id = array_cat_id.toArray(str_cat_id);

                    array_cat_name.add(mResepItem.getCategoryName());
                    str_cat_name = array_cat_name.toArray(str_cat_name);

                    array_cid.add(String.valueOf(mResepItem.getCId()));
                    str_cid = array_cid.toArray(str_cid);

                    array_image.add(String.valueOf(mResepItem.getNewsImage()));
                    str_image = array_image.toArray(str_image);

                    array_title.add(String.valueOf(mResepItem.getNewsHeading()));
                    str_title = array_title.toArray(str_title);

                    array_desc.add(String.valueOf(mResepItem.getNewsDescription()));
                    str_desc = array_desc.toArray(str_desc);

                    array_date.add(String.valueOf(mResepItem.getNewsDate()));
                    str_date = array_date.toArray(str_date);
                }

                setAdapterToRecyclerView();
            }

        }
    }

}
