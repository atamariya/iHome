package info.androidhive.speechtotext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.at.iHome.api.Command;
import com.at.iHome.logic.CommandHandler;
import com.ihome.BrowserActivity;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private TextToSpeech ttobj;
    private ProgressBar progressDialog;
    private int count = 0;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
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
        progressDialog = (ProgressBar)findViewById(R.id.progressBar1);
        progressDialog.setVisibility(View.GONE);
    }

    public void speakText(String toSpeak) {
        if (count != 0)
            return;

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
                    txtSpeechInput.setText(str);

                    List<Command> commands = CommandHandler.getInstance().execute(str);
                    count = commands.size();
                    if (count > 0) {
                        progressDialog.setVisibility(View.VISIBLE);
                        msg = "Command successful";
                    } else speakText("I don't understand the command " + str);

                    for (Command command : commands)
                        new NetTask().execute(command);
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class NetTask extends AsyncTask<Command, Void, Long> {
        protected Long doInBackground(Command... params) {
            String url = params[0].getUrl();
            String name = params[0].getName();
            String value = params[0].getValue();
            Map<String, String> header = params[0].getHeader();
            System.out.printf("Command: %s %s %s %s\n", url, name, value, header);

            Long status = new Long(0);
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            try {
                HttpPost post = new HttpPost(url);

                if (header != null) {
                    for (String key : header.keySet())
                        post.setHeader(key, header.get(key));
                }

                List<NameValuePair> parameters = new ArrayList<NameValuePair>();
                parameters.add(new BasicNameValuePair(name, value));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, HTTP.UTF_8);
                post.setEntity(formEntity);

                client.execute(post);
//                publishProgress((int) ((i / (float) count) * 100));
            } catch (IOException e) {
                e.printStackTrace();
                status = new Long("-1");
            }
//
//        // Escape early if cancel() is called
//        if (isCancelled()) return;
            client.close();
            return status;
        }

//    protected void onProgressUpdate(Integer... progress) {
////        setProgressPercent(progress[0]);
//    }

        protected void onPostExecute(Long result) {
            if (!new Long(0).equals(result)) {
                msg = "Command Failed. Please try again.";
            }
            count--;
            speakText(msg);
        }
    }
}

