package com.ihome.zones;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.at.iHome.api.Device;
import com.at.iHome.logic.CommandHandler;
import com.at.ihome.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI to edit devices in a zone or delete devices and zones.
 */
public class ZonesActivity extends Activity {

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
    int childPosition, groupPosition, itemType;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zones);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        listAdapter.setGroupPrefix("Zone: ");

		// setting list adapter
		expListView.setAdapter(listAdapter);

        editMode();
//        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                int itemType = ExpandableListView.getPackedPositionType(id);
//                int childPosition = ExpandableListView.getPackedPositionChild(id);
//                int groupPosition = ExpandableListView.getPackedPositionGroup(id);
//
//                return true;
//            }
//        });

        if (listDataHeader.size() > 0)
            expListView.expandGroup(0, true);
	}

    private void editMode() {
        expListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        expListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                itemType = ExpandableListView.getPackedPositionType(id);
                childPosition = ExpandableListView.getPackedPositionChild(id);
                groupPosition = ExpandableListView.getPackedPositionGroup(id);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        deleteSelectedItems();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    case R.id.action_edit:
                        editSelectedItems();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    case R.id.action_add:
                        addDevice();
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });
    }

    private void addDevice() {

    }

    private void deleteSelectedItems() {
        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            // delete group
            SharedPreferences sharedPref = getSharedPreferences("zones", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            String zone = listDataHeader.get(groupPosition);
            editor.remove(zone);
            editor.commit();
        }
        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            String zone = listDataHeader.get(groupPosition);
            com.at.iHome.api.Context context = com.at.iHome.api.Context.DEFAULT_CONTEXT;
            String child = (String) listAdapter.getChild(groupPosition, childPosition);
            if (zone.equals(context.getName())) {
                // If current group is default, delete device
                CommandHandler.getInstance().removeDevice(child);
            } else {
                // set device context to default
                CommandHandler.getInstance().setDeviceContext(child,
                        context);
            }
        }
        prepareListData();
        listAdapter.update(listDataHeader, listDataChild);
    }

    /**
     * Edit range in zone. Edit zone in devices.
     */
    private void editSelectedItems() {
        SharedPreferences sharedPref = getSharedPreferences("zones", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            // delete group
            String zone = listDataHeader.get(groupPosition);
//            editor.remove(zone);
        }
        editor.commit();
        prepareListData();
        expListView.invalidate();
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

        sharedPref = getSharedPreferences("zones", Context.MODE_PRIVATE);
        Map<String, ?> zones = sharedPref.getAll();
        int i = 0;
        for (String name : zones.keySet()) {
            listDataHeader.add(name);

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
        com.at.iHome.api.Context context = com.at.iHome.api.Context.DEFAULT_CONTEXT;
        listDataHeader.add(context.getName());
        List<Device> list = CommandHandler.getInstance().getDevices(context);
        List<String> children = new ArrayList<String>();
        for (Device device: list) {
            children.add(device.getName());
        }
        listDataChild.put(listDataHeader.get(i++), children);

	}

}
