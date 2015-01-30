package com.ihome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
    TypeListener typeListener = new TypeListener();
    ZoneListener zoneListener = new ZoneListener();
    String name, host;
    int type;
    Context zone;

    List<String> deviceTypes = CommandHandler.getInstance().getSupportedDevices();
    List<Context> zones = CommandHandler.getInstance().getKnownContexts();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);
        setTitle(R.string.device);

        Spinner types = (Spinner) findViewById(R.id.type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, deviceTypes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        types.setAdapter(adapter);
        types.setOnItemSelectedListener(typeListener);

        types = (Spinner) findViewById(R.id.zones);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Context> adapter1 = new ArrayAdapter<Context>(this, android.R.layout.simple_spinner_item,
                zones);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        types.setAdapter(adapter1);
        types.setOnItemSelectedListener(zoneListener);

        enableOk();

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void enableOk() {
        Button ok = (Button) findViewById(R.id.ok);
        ok.setEnabled(false);

        TextView txt = (TextView) findViewById(R.id.name);
        name = txt.getText().toString();
        txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                enableOk();
            }
        });
        txt = (TextView) findViewById(R.id.host);
        host = txt.getText().toString();
        txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                enableOk();
            }
        });

        if (zone == null || name.isEmpty() || host.isEmpty()) {
            return;
        }

        ok.setEnabled(true);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        CommandHandler instance = CommandHandler.getInstance();
        Device device = instance.createDevice(zone, name, host, type);
        instance.addDevice(device);
        finish();
    }


class TypeListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type = position;
        enableOk();
    }
}

class ZoneListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        zone = zones.get(position);
        enableOk();
    }
}
}