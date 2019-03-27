package id.web.skytacco.sysuka.base.fragment.tabLayout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.web.skytacco.sysuka.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KesukaanTabFragment extends Fragment {


    public KesukaanTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kesukaan, container, false);
    }

}
