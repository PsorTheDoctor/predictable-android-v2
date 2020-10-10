package com.wolkowycki.predictable.ui.wallet;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.wolkowycki.predictable.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class WalletFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wallet, container, false);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(root.getContext(),
                requireActivity().getSupportFragmentManager());
        ViewPager pager = root.findViewById(R.id.view_pager);
        pager.setAdapter(sectionsPagerAdapter);
        // pager.setCurrentItem(0);

        TabLayout tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        return root;
    }
}
