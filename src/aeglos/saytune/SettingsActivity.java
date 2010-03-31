/**
 *
 */
package aeglos.saytune;

import static aeglos.saytune.Common.SERVICE_ENABLED_PREFERENCE;
import static android.util.Log.INFO;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * @author plalloni
 */
public class SettingsActivity extends Activity {

    private static final Logger L = Logger.get(SettingsActivity.class);

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        L.log(INFO, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((CheckBox) findViewById(R.id.EnabledCheckBox))
            .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {
                    SharedPreferences sharedPreferences = Common
                        .getSharedPreferences(SettingsActivity.this);
                    sharedPreferences.edit().putBoolean(
                        SERVICE_ENABLED_PREFERENCE, isChecked).commit();
                }
            });
    }

}
