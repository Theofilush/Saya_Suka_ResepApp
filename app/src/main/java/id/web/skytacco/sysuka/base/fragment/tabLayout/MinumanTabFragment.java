package id.web.skytacco.sysuka.base.fragment.tabLayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import id.web.skytacco.sysuka.base.fragment.MinumanFragment;
import id.web.skytacco.sysuka.util.AppBarLayoutBehavior;

public class MinumanTabFragment extends Fragment {
    public static final String EXTRAS = "extras";
    private NavigationActivity nact;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    TabLayout.OnTabSelectedListener TlListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public MinumanTabFragment() {
    }

    @Override //belum diexx
    public void onAttach(Context activity) {
        super.onAttach(activity);
        nact = (NavigationActivity) activity;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_minuman_tab, container, false);
        AppBarLayout appBarLayout = v.findViewById(R.id.tab_appbar_layout);
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new AppBarLayoutBehavior());

        viewPager = v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new MinumanTabFragment.RedAdapter(getChildFragmentManager()));

        toolbar = v.findViewById(R.id.tab_toolbar);
        setupToolbar();

        tabLayout = v.findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.addOnTabSelectedListener(TlListener);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nact.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.duren_name));
        nact.setSupportActionBar(toolbar);
    }

    public class RedAdapter extends FragmentPagerAdapter {

        private String tresep = getResources().getString(R.string.duren_title);
        private String tkategori = getResources().getString(R.string.kategori_title);
        private final String[] tabs = {tresep, tkategori};

        RedAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MinumanFragment();
                case 1:
                    return new KategoriFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
