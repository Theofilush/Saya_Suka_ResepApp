package id.web.skytacco.sysuka.base.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import id.web.skytacco.sysuka.R;
import id.web.skytacco.sysuka.adapter.AboutAdapter;
import id.web.skytacco.sysuka.base.activity.NavigationActivity;

public class AboutFragment extends Fragment {
    ListView list;
    private Toolbar toolbar;
    private NavigationActivity mainActivity;
    //  private MainActivity mainActivity;
    Integer[] imageId = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_expand_less_black_24dp,
            R.drawable.ic_email_black_24dp,
            R.drawable.ic_info_black_24dp,
            R.drawable.ic_menu_share,
            R.drawable.ic_baseline_rate_review_24px,
            R.drawable.ic_baseline_apps_24px
    };
    String[] titleId = {
            "Nama Aplikasi",
            "Aplikasi Versi",
            "Email",
            "Info",
            "Rate",
            "More"
    };
    String[] subtitleId = {
            "Saya Suka",
            "Version 1.0",
            "Saya Suka, Kamu Suka - realth99@gmail.com",
            "2019 Handoyo Developer",
            "Klik List ini untuk memberikan Kami Bintang Yang terbaik",
            "Klik List ini untuk aplikasi Lainnya dari developer"
    };

    public AboutFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (NavigationActivity) activity;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);
        setupToolbar();

        AboutAdapter mAboutAdapter = new AboutAdapter(getActivity(), titleId, subtitleId, imageId);
        list = view.findViewById(R.id.list);
        list.setAdapter(mAboutAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    i.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name));
                    i.setType("text/plain");
                    startActivity(Intent.createChooser(i, "Share"));
                }
                if (position == 5) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
                if (position == 6) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Handoyo")));
                }
            }
        });
        return view;
    }

/*    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.navigation, menu);
    }*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity.setupNavigationDrawer(toolbar);
    }

    private void setupToolbar() {
        toolbar.setTitle(getString(R.string.about_name));
        mainActivity.setSupportActionBar(toolbar);
    }
}
