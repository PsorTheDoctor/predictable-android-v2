package com.wolkowycki.predictable.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.wolkowycki.predictable.R;

import java.util.HashMap;
import java.util.List;

public class SettingsListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;

    public SettingsListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.settings_question, null);
        }
        TextView group = (TextView) view.findViewById(R.id.settings_question);
        group.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.settings_answer, null);
        }
        TextView child = (TextView) view.findViewById(R.id.settings_answer);
        child.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
