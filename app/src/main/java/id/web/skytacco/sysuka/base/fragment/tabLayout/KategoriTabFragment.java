package id.web.skytacco.sysuka.base.fragment.tabLayout;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.base.activity.NavigationActivity;
import id.web.skytacco.sysuka.base.fragment.KategoriFragment;
import id.web.skytacco.sysuka.base.fragment.ResepFragment;
import id.web.skytacco.sysuka.util.AppBarLayoutBehavior;

public class KategoriTabFragment extends Fragment {
    public static int int_items = 2;
    private NavigationActivity na;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public KategoriTabFragment() {
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        na = (NavigationActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        AppBarLayout appBarLayout = v.findViewById(R.id.tab_appbar_layout);
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new AppBarLayoutBehavior());

        tabLayout = v.findViewById(R.id.tabs);
        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        viewPager.setCurrentItem(1);

        toolbar = v.findViewById(R.id.tab_toolbar);
        setupToolbar();


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        na.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.cat_name));
        na.setSupportActionBar(toolbar);
    }

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ResepFragment();
                case 1:
                    return new KategoriFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.resep_title);
                case 1:
                    return getResources().getString(R.string.kategori_title);
            }
            return null;
        }
    }
}
