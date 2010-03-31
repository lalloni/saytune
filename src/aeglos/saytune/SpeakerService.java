/**
 *
 */
package aeglos.saytune;

import static aeglos.saytune.Common.SERVICE_ENABLED_PREFERENCE;
import static android.speech.tts.TextToSpeech.QUEUE_ADD;

import java.util.Locale;

import com.android.music.IMediaPlaybackService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech;

/**
 * @author plalloni
 */
public class SpeakerService extends Service {

    private static final Logger log = Logger.get(SpeakerService.class);

    private boolean enabled;

    private TextToSpeech tts;

    private final OnSharedPreferenceChangeListener prefsListener;

    private MediaServiceHelper mediaServiceHelper;

    public SpeakerService() {
        mediaServiceHelper = new MediaServiceHelper(this);
        prefsListener = new OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if (SERVICE_ENABLED_PREFERENCE.equals(key)) {
                    enabled = prefs.getBoolean(SERVICE_ENABLED_PREFERENCE, true);
                    log.info("onSharedPreferenceChanged enabled: %s", enabled);
                }
            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // not exported
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log.info("onCreate");
        SharedPreferences prefs = Common.getSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(prefsListener);
        enabled = prefs.getBoolean(SERVICE_ENABLED_PREFERENCE, true);
        tts = new TextToSpeech(getBaseContext(), null);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        log.info("onStart: %s", intent);
        IMediaPlaybackService mediaPlayback = mediaServiceHelper.getMediaPlayback();
        try {
            if (enabled && mediaPlayback.isPlaying()) {
                log.info("Playing: %s", mediaPlayback.getPath());
                String language = mediaServiceHelper.getMediaLanguage();
                Locale defaultLocale = tts.getLanguage();
                try {
                    if (language != null) {
                        tts.setLanguage(new Locale(language));
                    }
                    tts.speak(intent.getStringExtra("track"), QUEUE_ADD, null);
                } finally {
                    tts.setLanguage(defaultLocale);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        log.info("onDestroy");
        Common.getSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(prefsListener);
        tts.shutdown();
        super.onDestroy();
    }

}
