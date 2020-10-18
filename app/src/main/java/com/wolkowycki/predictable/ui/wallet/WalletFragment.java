package com.wolkowycki.predictable.ui.wallet;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.wolkowycki.predictable.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class WalletFragment extends Fragment {

    private static final int DELAY = 2000;

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

        final FloatingTextButton ftb = root.findViewById(R.id.ftb);
        ftb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ftb.setTitle("Wallet Balance: 100 $");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ftb.setTitle("100 $");
                    }
                }, DELAY);
            }
        });
        return root;
    }
}
