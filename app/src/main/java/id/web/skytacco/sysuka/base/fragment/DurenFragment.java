package id.web.skytacco.sysuka.base.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import java.util.Objects;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.adapter.DurenAdapter;
import id.web.skytacco.sysuka.entity.DurenItem;
import id.web.skytacco.sysuka.network.JsonNetwork;
import id.web.skytacco.sysuka.util.DesainTampilan;
import id.web.skytacco.sysuka.util.Utils;

public class DurenFragment extends Fragment {
    RecyclerView recyclerView;
    List<DurenItem> arrayDurenItem;
    DurenAdapter mDurenAdapter;
    ArrayList<String> array_news, array_news_cat_name, array_cid, array_cat_id, array_cat_name, array_title, array_image, array_desc, array_date;
    String[] str_news, str_news_cat_name, str_cid, str_cat_id, str_cat_name, str_title, str_image, str_desc, str_date;
    JsonNetwork mJsonNetwork;
    int textLength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    private RelativeLayout relativeLayout;

    public DurenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_duren, container, false);
        setHasOptionsMenu(true);

        recyclerView = v.findViewById(R.id.recycler_view);
        relativeLayout = v.findViewById(R.id.no_network);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Utils.NUM_OF_COLUMNS));
        DesainTampilan mDesainTampilan = new DesainTampilan(Objects.requireNonNull(getActivity()), R.dimen.desain_tampilan);
        recyclerView.addItemDecoration(mDesainTampilan);

        arrayDurenItem = new ArrayList<>();
        array_news = new ArrayList<>();
        array_news_cat_name = new ArrayList<>();
        array_cid = new ArrayList<>();
        array_cat_id = new ArrayList<>();
        array_cat_name = new ArrayList<>();
        array_title = new ArrayList<>();
        array_image = new ArrayList<>();
        array_desc = new ArrayList<>();
        array_date = new ArrayList<>();

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
            new DurenFragment.MyTask().execute(Utils.SERVER_URL + "/api.php?latest_news=" + Utils.NUM_OF_RECENT_RECIPES);
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
                        new DurenFragment.MyTask().execute(Utils.SERVER_URL + "/api.php?latest_news=50");
                    }
                }, 1500);
            }
        });

        return v;
    }

    public void clearData() {
        int size = this.arrayDurenItem.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.arrayDurenItem.remove(0);
            }

            mDurenAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    public void setAdapterToRecyclerView() {
        mDurenAdapter = new DurenAdapter(getActivity(), arrayDurenItem);
        recyclerView.setAdapter(mDurenAdapter);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        //SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
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
                arrayDurenItem.clear();
                for (int i = 0; i < str_title.length; i++) {
                    if (textLength <= str_title[i].length()) {
                        if (str_title[i].toLowerCase().contains(newText.toLowerCase())) {
                            DurenItem objItem = new DurenItem();
                            objItem.setCategoryName((str_cat_name[i]));
                            objItem.setCatId(str_cat_id[i]);
                            objItem.setCId(str_cid[i]);
                            objItem.setNewsDate(str_date[i]);
                            objItem.setNewsDescription(str_desc[i]);
                            objItem.setNewsHeading(str_title[i]);
                            objItem.setNewsImage(str_image[i]);
                            arrayDurenItem.add(objItem);
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
        if (menuItem.getItemId() == android.R.id.home) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
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
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!!", Toast.LENGTH_SHORT).show();
                relativeLayout.setVisibility(View.VISIBLE);
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    JSONArray jsonArray = mainJson.getJSONArray(Utils.CATEGORY_ARRAY_NAME);
                    //JSONObject objJson = null;
                    JSONObject objJson;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        DurenItem objItem = new DurenItem();

                        objItem.setCId(objJson.getString(Utils.CATEGORY_ITEM_CID));
                        objItem.setCategoryName(objJson.getString(Utils.CATEGORY_ITEM_NAME));
                        objItem.setCatId(objJson.getString(Utils.CATEGORY_ITEM_CAT_ID));
                        objItem.setNewsImage(objJson.getString(Utils.CATEGORY_ITEM_NEWSIMAGE));
                        objItem.setNewsHeading(objJson.getString(Utils.CATEGORY_ITEM_NEWSHEADING));
                        objItem.setNewsDescription(objJson.getString(Utils.CATEGORY_ITEM_NEWSDESCRI));
                        objItem.setNewsDate(objJson.getString(Utils.CATEGORY_ITEM_NEWSDATE));

                        arrayDurenItem.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < arrayDurenItem.size(); j++) {
                    DurenItem mDurenItem = arrayDurenItem.get(j);

                    array_cat_id.add(mDurenItem.getCatId());
                    array_cat_name.add(mDurenItem.getCategoryName());
                    array_cid.add(String.valueOf(mDurenItem.getCId()));
                    array_image.add(String.valueOf(mDurenItem.getNewsImage()));
                    array_title.add(String.valueOf(mDurenItem.getNewsHeading()));
                    array_desc.add(String.valueOf(mDurenItem.getNewsDescription()));
                    array_date.add(String.valueOf(mDurenItem.getNewsDate()));

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
