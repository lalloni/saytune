/**
 *
 */
package aeglos.saytune.activities;

import static java.lang.System.currentTimeMillis;

import java.util.Random;

import aeglos.android.common.Logger;
import aeglos.saytune.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author plalloni
 */
public class MainActivity extends Activity {

    private static final Logger log = Logger.get(MainActivity.class);

    private static final int MY_DATA_CHECK_CODE = new Random(currentTimeMillis()).nextInt();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        log.debug("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button settingsButton = (Button) findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SettingsActivity.class));
            }
        });
        log.info("Checking for TTS data installed");
        startActivityForResult(new Intent().setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA), MY_DATA_CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                startActivity(new Intent().setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA));
            } else {
                log.info("TTS data installed");
            }
        }
    }

}
