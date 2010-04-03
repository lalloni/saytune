/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 31/03/2010 18:09:55
 */
package aeglos.saytune;

import aeglos.android.common.Logger;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.android.music.IMediaPlaybackService;

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 * @since 31/03/2010 18:09:55
 */
public class LanguageHelper {

    private static final Logger log = Logger.get(LanguageHelper.class);

    public static String getCurrentMediaLanguage(Context context, IMediaPlaybackService service) {
        String language = null;
        try {
            Cursor cursor = context.getContentResolver().query(Uri.parse(service.getPath()), null, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    //language = read(new File(cursor.getString(cursor.getColumnIndex(DATA)))).getTag().getFirst(LANGUAGE);
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            log.error(e, "Error reading language from file tag");
        }
        log.info("Language: %s", language);
        return language;
    }

}
