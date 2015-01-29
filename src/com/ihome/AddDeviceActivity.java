package com.ihome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.at.iHome.api.Context;
import com.at.iHome.api.Device;
import com.at.iHome.logic.CommandHandler;
import com.at.iHome.logic.DenonAVR;
import com.at.ihome.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand.Tamariya on 29-Jan-15.
 */
public class AddDeviceActivity extends Activity implements View.OnClickListener {
    TypeListener typeListener;
    ZoneListener zoneListener;
    String name, type, zone, host;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);
        setTitle(R.string.device);

        Spinner types = (Spinner) findViewById(R.id.type);
        List<String> type = new ArrayList<String>();
        type.add("Cisco DCS-930L");
        type.add("Denon AVR");
        type.add("XBMC");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        types.setAdapter(adapter);
        types.setOnItemSelectedListener(typeListener);

        types = (Spinner) findViewById(R.id.zones);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Context> adapter1 = new ArrayAdapter<Context>(this, android.R.layout.simple_spinner_item,
                CommandHandler.getInstance().getKnownContexts());
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        types.setAdapter(adapter1);
        types.setOnItemSelectedListener(zoneListener);

        Button ok = (Button) findViewById(R.id.ok);
        ok.setEnabled(false);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = new Context(zone);
        Device device = new DenonAVR(name, host);
        CommandHandler.getInstance().addDevice(device);
    }
}

class TypeListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
}

class ZoneListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
}