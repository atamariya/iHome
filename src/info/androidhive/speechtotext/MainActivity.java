package info.androidhive.speechtotext;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.at.iHome.logic.CommandHandler;
import com.ihome.BrowserActivity;

import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

	private TextView txtSpeechInput;
	private ImageButton btnSpeak;
	private final int REQ_CODE_SPEECH_INPUT = 100;

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
	 * */
	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
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
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {
                List<String> result = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				txtSpeechInput.setText(result.get(0));
                CommandHandler.getInstance().execute(result.get(0));
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

}
