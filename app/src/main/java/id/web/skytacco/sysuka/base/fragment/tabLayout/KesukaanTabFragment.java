package id.web.skytacco.sysuka.base.fragment.tabLayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.web.skytacco.sysuka.Database.DatabaseHelper;
import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.adapter.FavoriteAdapter;
import id.web.skytacco.sysuka.entity.FavoriteItem;
import id.web.skytacco.sysuka.network.JsonNetwork;
import id.web.skytacco.sysuka.util.DesainTampilan;
import id.web.skytacco.sysuka.util.Utils;

public class KesukaanTabFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseHelper databaseHandler;
    FavoriteAdapter adapterFavorite;
    LinearLayout relativeLayout;
    JsonNetwork mUtils;
    List<FavoriteItem> arrayItemFavorite;
    ArrayList<String> array_news, array_news_cat_name, array_cid, array_cat_id, array_cat_name, array_title, array_image, array_desc, array_date;
    String[] str_news, str_news_cat_name, str_cid, str_cat_id, str_cat_name, str_title, str_image, str_desc, str_date;
    int textLength = 0;
    private DatabaseHelper.DatabaseManager mDatabaseManager;
    private RelativeLayout rootLayout;

    public KesukaanTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kesukaan, container, false);
        setHasOptionsMenu(true);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Utils.NUM_OF_COLUMNS));
        DesainTampilan itemDecoration = new DesainTampilan(Objects.requireNonNull(getActivity()), R.dimen.desain_tampilan);
        recyclerView.addItemDecoration(itemDecoration);

        relativeLayout = v.findViewById(R.id.relativeLayout);
        databaseHandler = new DatabaseHelper(getActivity());
        mDatabaseManager = DatabaseHelper.DatabaseManager.INSTANCE;
        mDatabaseManager.init(getActivity());
        mUtils = new JsonNetwork(getActivity().getApplicationContext());

        arrayItemFavorite = databaseHandler.getAllData();
        adapterFavorite = new FavoriteAdapter(getActivity(), arrayItemFavorite);
        recyclerView.setAdapter(adapterFavorite);
        if (arrayItemFavorite.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    public void onDestroy() {
        if (!mDatabaseManager.isDatabaseClosed())
            mDatabaseManager.closeDatabase();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mDatabaseManager.isDatabaseClosed())
            mDatabaseManager.closeDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        arrayItemFavorite = databaseHandler.getAllData();
        adapterFavorite = new FavoriteAdapter(getActivity(), arrayItemFavorite);
        recyclerView.setAdapter(adapterFavorite);
        if (arrayItemFavorite.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }

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

        for (int j = 0; j < arrayItemFavorite.size(); j++) {
            FavoriteItem objAllBean = arrayItemFavorite.get(j);

            array_cat_id.add(objAllBean.getCatId());
            str_cat_id = array_cat_id.toArray(str_cat_id);

            array_cid.add(String.valueOf(objAllBean.getCId()));
            str_cid = array_cid.toArray(str_cid);

            array_cat_name.add(objAllBean.getCategoryName());
            str_cat_name = array_cat_name.toArray(str_cat_name);

            array_title.add(String.valueOf(objAllBean.getNewsHeading()));
            str_title = array_title.toArray(str_title);

            array_image.add(String.valueOf(objAllBean.getNewsImage()));
            str_image = array_image.toArray(str_image);

            array_desc.add(String.valueOf(objAllBean.getNewsDesc()));
            str_desc = array_desc.toArray(str_desc);

            array_date.add(String.valueOf(objAllBean.getNewsDate()));
            str_date = array_date.toArray(str_date);
        }
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
                arrayItemFavorite.clear();
                for (int i = 0; i < str_title.length; i++) {
                    if (textLength <= str_title[i].length()) {
                        if (str_title[i].toLowerCase().contains(newText.toLowerCase())) {
                            FavoriteItem objItem = new FavoriteItem();
                            objItem.setCatId(str_cat_id[i]);
                            objItem.setCId(str_cid[i]);
                            objItem.setCategoryName(str_cat_name[i]);
                            objItem.setNewsHeading(str_title[i]);
                            objItem.setNewsImage(str_image[i]);
                            objItem.setNewsDesc(str_desc[i]);
                            objItem.setNewsDate(str_date[i]);
                            arrayItemFavorite.add(objItem);
                        }
                    }
                }
                adapterFavorite = new FavoriteAdapter(getActivity(), arrayItemFavorite);
                recyclerView.setAdapter(adapterFavorite);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return super.onOptionsItemSelected(menuItem);
    }
}
