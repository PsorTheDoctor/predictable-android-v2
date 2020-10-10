package com.wolkowycki.predictable.ui.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolkowycki.predictable.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SellFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static SellFragment newInstance(int index) {
        SellFragment fragment = new SellFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sell, container, false);
        return root;
    }
}