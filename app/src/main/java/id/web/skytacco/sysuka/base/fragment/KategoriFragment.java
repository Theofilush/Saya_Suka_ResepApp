package id.web.skytacco.sysuka.base.fragment;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.Objects;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.adapter.KategoriAdapter;
import id.web.skytacco.sysuka.entity.KategoriItem;
import id.web.skytacco.sysuka.network.JsonNetwork;
import id.web.skytacco.sysuka.util.DesainTampilan2;
import id.web.skytacco.sysuka.util.Utils;

public class KategoriFragment extends Fragment {
    RecyclerView recyclerView;
    List<KategoriItem> arrayItemCategory;
    KategoriAdapter adapterCategory;
    ArrayList<String> array_cat_id, array_cat_name, array_cat_image;
    String[] str_cat_id, str_cat_name, str_cat_image;
    int textLength = 0;
    SwipeRefreshLayout swipeRefreshLayout = null;
    private KategoriItem itemCategory;
    private RelativeLayout rootLayout;
    private RelativeLayout relativeLayout;

    public KategoriFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kategori2, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        relativeLayout = v.findViewById(R.id.no_network);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DesainTampilan2(1, 0, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayItemCategory = new ArrayList<>();
        array_cat_id = new ArrayList<>();
        array_cat_name = new ArrayList<>();
        array_cat_image = new ArrayList<>();
        str_cat_id = new String[array_cat_id.size()];
        str_cat_name = new String[array_cat_image.size()];
        str_cat_image = new String[array_cat_name.size()];

        if (JsonNetwork.isNetworkAvailable(Objects.requireNonNull(getActivity()))) {
            new MyTask().execute(Utils.SERVER_URL + "/api.php");
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.failed_connect_network), Toast.LENGTH_SHORT).show();
            relativeLayout.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                array_cat_id.clear();
                array_cat_name.clear();
                array_cat_image.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        clearData();
                        new MyTask().execute(Utils.SERVER_URL + "/api.php");
                    }
                }, 1500);
            }
        });

        return v;
    }

    public void clearData() {
        int size = this.arrayItemCategory.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.arrayItemCategory.remove(0);
            }
            adapterCategory.notifyItemRangeRemoved(0, size);
        }
    }

    public void setAdapterToRecyclerView() {
        adapterCategory = new KategoriAdapter(getActivity(), arrayItemCategory);
        recyclerView.setAdapter(adapterCategory);
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
                arrayItemCategory.clear();
                for (int i = 0; i < str_cat_name.length; i++) {
                    if (textLength <= str_cat_name[i].length()) {
                        if (str_cat_name[i].toLowerCase().contains(newText.toLowerCase())) {
                            KategoriItem objItem = new KategoriItem();
                            objItem.setCategoryId(Integer.parseInt(str_cat_id[i]));
                            objItem.setCategoryName(str_cat_name[i]);
                            objItem.setCategoryImageurl(str_cat_image[i]);
                            arrayItemCategory.add(objItem);
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
                    JSONArray jsonArray = mainJson.getJSONArray(Utils.CATEGORY_ARRAY_NAME);
                    JSONObject objJson = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        objJson = jsonArray.getJSONObject(i);
                        KategoriItem objItem = new KategoriItem();
                        objItem.setCategoryName(objJson.getString(Utils.CATEGORY_NAME));
                        objItem.setCategoryId(objJson.getInt(Utils.CATEGORY_CID));
                        objItem.setCategoryImageurl(objJson.getString(Utils.CATEGORY_IMAGE));
                        arrayItemCategory.add(objItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int j = 0; j < arrayItemCategory.size(); j++) {
                    itemCategory = arrayItemCategory.get(j);
                    array_cat_id.add(String.valueOf(itemCategory.getCategoryId()));
                    array_cat_image.add(itemCategory.getCategoryName());
                    array_cat_name.add(itemCategory.getCategoryImageurl());
                    str_cat_id = array_cat_id.toArray(str_cat_id);
                    str_cat_name = array_cat_image.toArray(str_cat_name);
                    str_cat_image = array_cat_name.toArray(str_cat_image);
                }
                setAdapterToRecyclerView();
            }
        }
    }
}
