package com.wolkowycki.predictable.ui.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.wolkowycki.predictable.utils.LocalStore;
import com.wolkowycki.predictable.R;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class WalletFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private Animation fabAnim;

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

        coordinatorLayout = root.findViewById(R.id.coordinator);
        fab = root.findViewById(R.id.fab);
        fabAnim = AnimationUtils.loadAnimation(root.getContext(), R.anim.fab_animation);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSnackbar();
            }
        });
        return root;
    }

    private void showSnackbar() {
        float balance = LocalStore.loadBalance(requireActivity(), "balance");

        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Wallet balance: " + balance + " $",
                Snackbar.LENGTH_SHORT);
        // View snackView = snackbar.getView();
        // TextView snackTxt = snackView.findViewById(R.id.snackbar_text);
        snackbar.show();
        fab.startAnimation(fabAnim);
    }
}
