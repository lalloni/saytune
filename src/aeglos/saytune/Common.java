/**
 *
 */
package aeglos.saytune;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author plalloni
 */
public class Common {

    public static final String SAY_TUNE_PREFERENCES = "SayTune Preferences";

    public static final String SERVICE_ENABLED_PREFERENCE = "enabled";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SAY_TUNE_PREFERENCES, 0);
    }

}
