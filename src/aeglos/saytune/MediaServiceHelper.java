/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 31/03/2010 18:09:55
 */
package aeglos.saytune;

import static android.content.Context.BIND_AUTO_CREATE;
import static org.jaudiotagger.tag.FieldKey.LANGUAGE;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.concurrent.Semaphore;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;

import com.android.music.IMediaPlaybackService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 * @since 31/03/2010 18:09:55
 */
public class MediaServiceHelper {

    private static final Logger log = Logger.get(MediaServiceHelper.class);

    private final Context context;

    private WeakReference<IMediaPlaybackService> mediaPlaybackReference = new WeakReference<IMediaPlaybackService>(null);

    public MediaServiceHelper(Context context) {
        this.context = context;
    }

    public String getMediaLanguage() {
        String language;
        try {
            final Uri contentUri = Uri.parse(getMediaPlayback().getPath());
            Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
            language = null;
            if (cursor.moveToFirst()) {
                AudioFile audioFile = AudioFileIO.read(new File(cursor.getString(cursor.getColumnIndex("_data"))));
                language = audioFile.getTag().getFirst(LANGUAGE);
            }
        } catch (Exception e) {
            language = null;
            log.error(e, "Error reading language");
        }
        log.info("Language: %s", language);
        return language;
    }

    public IMediaPlaybackService getMediaPlayback() {
        log.info("getMediaPlayback");
        IMediaPlaybackService mediaPlayback = mediaPlaybackReference.get();
        if (mediaPlayback == null) {
            final Semaphore s = new Semaphore(0);
            context.bindService(new Intent().setClassName("com.android.music", "com.android.music.MediaPlaybackService"),
                new ServiceConnection() {
                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        mediaPlaybackReference.clear();
                    }

                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        mediaPlaybackReference = new WeakReference<IMediaPlaybackService>(IMediaPlaybackService.Stub
                            .asInterface(service));
                        s.release();
                    }
                }, BIND_AUTO_CREATE);
            log.info("Waiting service connection");
            s.acquireUninterruptibly();
            log.info("Service connected");
            mediaPlayback = mediaPlaybackReference.get();
        }
        return mediaPlayback;
    }

}
