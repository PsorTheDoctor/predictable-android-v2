package com.wolkowycki.predictable.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.wolkowycki.predictable.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsFragment extends Fragment {

    private LinearLayout shareBtn;
    private LinearLayout donateBtn;

    private ExpandableListView listView;
    private SettingsListAdapter listAdapter;
    private List<String> listHeader;
    private HashMap<String, List<String>> listHash;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        shareBtn = (LinearLayout) root.findViewById(R.id.btn_share);
        donateBtn = (LinearLayout) root.findViewById(R.id.btn_donate);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String content = "Check predictable app: *link to Google Play here*";
                intent.putExtra(Intent.EXTRA_TEXT, content);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, null));
            }
        });

        donateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        listView = (ExpandableListView) root.findViewById(R.id.settings_list);
        initData();
        listAdapter = new SettingsListAdapter(getContext(), listHeader, listHash);
        listView.setAdapter(listAdapter);

        return root;
    }

    public void openDialog() {
        DonationDialog dialog = new DonationDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), "Donation dialog");
    }

    private void initData() {
        listHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listHeader.add("How does it work?");
        listHeader.add("Can I buy coins directly from you?");
        listHeader.add("I have an idea for further development, can I contact you?");

        List<String> description = new ArrayList<>();
        description.add("We use artificial intelligence methods called deep neural networks to predict future cryptocurrency prices.");
        listHash.put(listHeader.get(0), description);

        List<String> answer = new ArrayList<>();
        answer.add("No, we don't sell coins.");
        listHash.put(listHeader.get(1), answer);

        List<String> contact = new ArrayList<>();
        contact.add("We're open to ideas that may improve our systems. If you have any, be sure you can contact us.");
        listHash.put(listHeader.get(2), contact);
    }
}
