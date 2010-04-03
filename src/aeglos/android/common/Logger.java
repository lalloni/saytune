/**
 *
 */
package aeglos.android.common;

import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;
import static android.util.Log.isLoggable;
import static java.lang.String.format;
import static java.util.Collections.synchronizedMap;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

/**
 * @author plalloni
 */
public class Logger {

    private final String tag;

    private static final Map<String, Logger> loggers = synchronizedMap(new HashMap<String, Logger>());

    public static Logger get(String tag) {
        Logger logger = loggers.get(tag);
        if (logger == null) {
            logger = new Logger(tag);
            loggers.put(tag, logger);
        }
        return logger;
    }

    public static Logger get(Class<?> tag) {
        return get(tag.getSimpleName());
    }

    private Logger(String tag) {
        this.tag = tag;
    }

    public void log(int level, String message, Object... arguments) {
        if (isLoggable(tag, level)) {
            Log.println(level, tag, format(message, arguments));
        }
    }

    public void debug(String message, Object... arguments) {
        if (isLoggable(tag, DEBUG)) {
            Log.d(tag, format(message, arguments));
        }
    }

    public void verbose(String message, Object... arguments) {
        if (isLoggable(tag, VERBOSE)) {
            Log.v(tag, format(message, arguments));
        }
    }

    public void info(String message, Object... arguments) {
        if (isLoggable(tag, INFO)) {
            Log.i(tag, format(message, arguments));
        }
    }

    public void warn(Throwable error, String message, Object... arguments) {
        if (isLoggable(tag, WARN)) {
            Log.w(tag, format(message, arguments), error);
        }
    }

    public void warn(String message, Object... arguments) {
        if (isLoggable(tag, WARN)) {
            Log.w(tag, format(message, arguments));
        }
    }

    public void error(Throwable error, String message, Object... arguments) {
        if (isLoggable(tag, ERROR)) {
            Log.e(tag, format(message, arguments), error);
        }
    }

    public void error(String message, Object... arguments) {
        if (isLoggable(tag, ERROR)) {
            Log.e(tag, format(message, arguments));
        }
    }

}
