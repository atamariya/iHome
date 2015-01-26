package com.ihome.zones;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.at.iHome.api.Device;
import com.at.iHome.logic.CommandHandler;
import com.at.ihome.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ZonesActivity extends Activity {

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zones);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Expanded",
						Toast.LENGTH_SHORT).show();
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Collapsed",
						Toast.LENGTH_SHORT).show();

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ listDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});

        if (listDataHeader.size() == 1)
            expListView.expandGroup(0, true);
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding groups
        SharedPreferences sharedPref = getSharedPreferences("range", Context.MODE_PRIVATE);
        int range = sharedPref.getInt("range", 5);

        sharedPref = getSharedPreferences("zones1", Context.MODE_PRIVATE);
        Map<String, ?> zones = sharedPref.getAll();
        int i = 0;
        for (String name : zones.keySet()) {
            listDataHeader.add("Zone: " + name);

            // Add children
            com.at.iHome.api.Context context = new com.at.iHome.api.Context(name);
            List<Device> list = CommandHandler.getInstance().getDevices(context);
            List<String> children = new ArrayList<String>();
            for (Device device: list) {
                children.add(device.getName());
            }
            listDataChild.put(listDataHeader.get(i++), children);
        }

        // Group everything else in default zone
        String name = "Default";
        listDataHeader.add("Zone: " + name);

        // Add children
        com.at.iHome.api.Context context = com.at.iHome.api.Context.DEFAULT_CONTEXT;
        List<Device> list = CommandHandler.getInstance().getDevices(context);
        List<String> children = new ArrayList<String>();
        for (Device device: list) {
            children.add(device.getName());
        }
        listDataChild.put(listDataHeader.get(i++), children);

	}
}
