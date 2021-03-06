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
import id.web.skytacco.sysuka.base.fragment.KesukaanFragment;
import id.web.skytacco.sysuka.util.AppBarLayoutBehavior;

public class KesukaanTabFragment extends Fragment {
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public static int int_items = 1;
    private NavigationActivity nact;
    private Toolbar toolbar;

    public KesukaanTabFragment() {
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        nact = (NavigationActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_kesukaan, container, false);

        AppBarLayout appBarLayout = v.findViewById(R.id.tab_appbar_layout);
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new AppBarLayoutBehavior());

        tabLayout = v.findViewById(R.id.tabs);
        viewPager = v.findViewById(R.id.viewpager);

        toolbar = v.findViewById(R.id.tab_toolbar);
        setupToolbar();

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

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
        nact.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.favorite_name));
        nact.setSupportActionBar(toolbar);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new KesukaanFragment();
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
                    return getResources().getString(R.string.kesukaan_title);
            }
            return null;
        }
    }
}
