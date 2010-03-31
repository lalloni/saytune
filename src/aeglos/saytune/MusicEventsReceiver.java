/**
 *
 */
package aeglos.saytune;

import static android.util.Log.INFO;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author plalloni
 */
public class MusicEventsReceiver extends BroadcastReceiver {

    private static final Logger logger = Logger.get(MusicEventsReceiver.class);

    @Override
    public void onReceive(final Context context, Intent intent) {
        logger.log(INFO, "onReceive: %s", intent.getAction());
        Intent say = new Intent(context, SpeakerService.class);
        say.putExtras(intent.getExtras());
        context.startService(say);
    }

}
