/**
 *
 */
package aeglos.saytune.activities;

import aeglos.android.common.Logger;
import aeglos.saytune.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.speech.tts.TextToSpeech;

/**
 * @author plalloni
 */
public class SettingsActivity extends PreferenceActivity {

    private static final Logger log = Logger.get(SettingsActivity.class);

    private static final int MY_DATA_CHECK_CODE = 327234879;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
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
