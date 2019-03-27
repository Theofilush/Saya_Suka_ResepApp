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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.base.activity.NavigationActivity;
import id.web.skytacco.sysuka.base.fragment.KategoriFragment;
import id.web.skytacco.sysuka.base.fragment.ResepFragment;

import static android.content.ContentValues.TAG;

public class HomeTabFragment extends Fragment {
    public static final String EXTRAS = "extras";
    private NavigationActivity na;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HomeTabFragment() {
    }
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //AppBarLayout appBarLayout = view.findViewById(R.id.tabapplayout);
        //((LinearLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new AppBarLayoutBehavior());

        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new RedAdapter(getChildFragmentManager()));

        tabLayout = view.findViewById(R.id.viewPagerTab);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.addOnTabSelectedListener(TlListener);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String page = getArguments().getString(EXTRAS);

            Log.e(TAG, "onCreateView: halaman fragment " + page);
        }
    }
    public class RedAdapter extends FragmentPagerAdapter {

        private String tresep = getResources().getString(R.string.resep_title);
        private String tkategori = getResources().getString(R.string.kategori_title);
        private final String tabs[] = {tresep, tkategori};

        RedAdapter(FragmentManager fm) {
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
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
