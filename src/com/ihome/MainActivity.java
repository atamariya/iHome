package com.ihome;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.at.iHome.api.Command;
import com.at.iHome.api.ZoneException;
import com.at.iHome.logic.CommandHandler;
import com.at.ihome.R;
import com.ihome.zones.ZoneOverrideDialog;
import com.ihome.zones.ZonesActivity;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends Activity {

    private TextView txtSpeechInput, zoneText;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private TextToSpeech ttobj;
    private ProgressBar progressDialog;
    private String msg;
    private SearchView searchView;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        zoneText = (TextView) findViewById(R.id.zoneTxt);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        // hide the action bar
//		getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				browseDevices();
                promptSpeechInput();
            }
        });

        ttobj = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            ttobj.setLanguage(Locale.UK);
                        }
                    }
                });
        progressDialog = (ProgressBar) findViewById(R.id.progressBar1);
        progressDialog.setVisibility(View.GONE);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processCommand(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        loadZoneInfo();
    }

    public void speakText(String toSpeak) {
        progressDialog.setVisibility(View.GONE);
        txtSpeechInput.setText(null);
        Toast.makeText(getApplicationContext(), toSpeak,
                Toast.LENGTH_SHORT).show();
        ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

    }

    private void browseDevices() {
        Intent intent = new Intent(this, BrowserActivity.class);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.errorSwitchingRouter),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void modifyZones() {
        Intent intent = new Intent(this, ZonesActivity.class);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.errorSwitchingRouter),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//				getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    List<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String str = result.get(0);
                    //txtSpeechInput.setText(str);
                    searchView.setQuery(str, false);

                    processCommand(str);
                }
                break;
            }

        }
    }

    private  void loadZoneInfo() {
        SharedPreferences sharedPref = getSharedPreferences("zones", Context.MODE_PRIVATE);
        Map<String, ?> zones = sharedPref.getAll();
        for (String name : zones.keySet()) {
            com.at.iHome.api.Context context = new com.at.iHome.api.Context(name, sharedPref.getInt(name, 0));
            CommandHandler.getInstance().addContext(context);
        }
    }

    public void processCommand(String str) {
        processCommand(str, false);
    }

    public void processCommand(String str, boolean useDefaultContext) {
        // Determine zone context
        int rssi = wifiManager.getConnectionInfo().getRssi();
        SharedPreferences sharedPref = getSharedPreferences("range", Context.MODE_PRIVATE);
        int range = sharedPref.getInt("range", 5);

        sharedPref = getSharedPreferences("zones", Context.MODE_PRIVATE);
        Map<String, ?> zones = sharedPref.getAll();
        com.at.iHome.api.Context context = new com.at.iHome.api.Context(rssi, range);
        for (String name : zones.keySet()) {
            com.at.iHome.api.Context context1 = new com.at.iHome.api.Context(name, sharedPref.getInt(name, 0));
            if (context.equals(context1)) {
                context = new com.at.iHome.api.Context(name);
                break;
            }
        }

        // No zone created yet.
        if (context.getName() == null || useDefaultContext) {
            context = com.at.iHome.api.Context.DEFAULT_CONTEXT;
        }
        List<Command> commands = null;
        try {
            zoneText.setText("Zone: " + context.getName());
            commands = CommandHandler.getInstance().execute(context, str);
        } catch (ZoneException e) {
            speakText(getString(R.string.override_prompt));
            DialogFragment dialog = new ZoneOverrideDialog();

            Bundle args = new Bundle();
            args.putString("cmd", str);
            dialog.setArguments(args);

            dialog.show(getFragmentManager(), "tag");

            return;
        }

        int count = commands.size();
        if (count > 0) {
            progressDialog.setVisibility(View.VISIBLE);
            msg = "Command successful";

            for (Iterator<Command> iter = commands.iterator(); iter.hasNext(); ) {
                Command command = iter.next();
                if (command.isView()) {
                    // Streaming doesn't work with webview
                    Uri uri = Uri.parse(command.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("Authorization", "Basic " + authorizationBase64);
//                                i.putExtra(Browser.EXTRA_HEADERS, bundle);
                    startActivity(intent);

                    iter.remove();
                }
                if (command.isSetting()) {
                    txtSpeechInput.setText(String.format("%s %s %d", command.getName(), command.getValue(),
                            rssi));

                    com.at.iHome.api.Context context1 = new com.at.iHome.api.Context(command.getValue(), rssi);
                    CommandHandler.getInstance().addContext(context1);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(context1.getName(), rssi);
                    editor.commit();

                    iter.remove();
                }
            }

            new NetTask().execute(commands.toArray(new Command[commands.size()]));
        } else speakText("I don't understand the command " + str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                modifyZones();
                return true;
            case R.id.action_add:
                addDevice();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addDevice() {
            Intent intent = new Intent(this, AddDeviceActivity.class);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.errorSwitchingRouter),
                        Toast.LENGTH_SHORT).show();
            }
    }

    class NetTask extends AsyncTask<Command, Integer, Long> {
        protected Long doInBackground(Command... params) {
            Long status = new Long(0);
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            boolean breakChain = false;
            for (int i = 0; i < params.length && status != new Long("-1") && !isCancelled(); i++) {
                if (params[i].isChained() && breakChain) {
                    continue;
                }
                final String url = params[i].getUrl();
                String name = params[i].getName();
                String value = params[i].getValue();
                Map<String, String> header = params[i].getHeader();
                System.out.printf("Command: %s %s %s %s\n", url, name, value, header);

                try {
                    HttpPost post = new HttpPost(url);

                    if (header != null) {
                        for (String key : header.keySet())
                            post.setHeader(key, header.get(key));
                    }

                    HttpEntity formEntity = null;
                    if (params[i].isJson()) {
                        formEntity = new StringEntity(value);
                    } else {
                        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                        parameters.add(new BasicNameValuePair(name, value));
                        formEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                    }
                    post.setEntity(formEntity);

                    formEntity = client.execute(post).getEntity();

                    // If it's a macro (chained command), process results
                    String str = EntityUtils.toString(formEntity);
                    if (params[i].isChained()) {
                        params[i + 1].setValue(params[i + 1].getResponseProcessor().process(str));
                    } else if (params[i].getResponseProcessor() != null) {
                        params[i].getResponseProcessor().process(str);
                    }
                    publishProgress((int) ((i / (float) params.length) * 100));
                } catch (IOException e) {
                    e.printStackTrace();
                    msg = "Device unavailable";
                    breakChain = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = new Long("-1");
                    breakChain = true;
                }
            }

            client.close();
            return status;
        }

        protected void onProgressUpdate(Integer... progress) {
//            progressDialog.setProgress(progress[0]);
        }

        protected void onPostExecute(Long result) {
            if (!new Long(0).equals(result)) {
                msg = "Command Failed. Please try again.";
            }
            speakText(msg);
        }
    }
}

