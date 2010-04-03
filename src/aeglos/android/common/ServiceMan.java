/**
 *
 */
package aeglos.android.common;

import static java.lang.String.format;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

/**
 * @author plalloni
 */
public abstract class ServiceMan<T> {

    private String _tag;
    private final Context context;
    private final Class<?> serviceType;
    private final Intent intent;
    private final Method method;
    private WeakReference<T> serviceReference;
    private ServiceConnection serviceConnection;

    public ServiceMan(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        this.serviceType = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            this.method = serviceType.getDeclaredClasses()[0].getMethod("asInterface", new Class[] { IBinder.class });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean call(final Closure<T> closure) {
        if (serviceReference == null || serviceReference.get() == null) {
            serviceConnection = new ServiceConnection() {
                @SuppressWarnings("unchecked")
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    Log.i(tag(), "onServiceConnected: " + name);
                    try {
                        serviceReference = new WeakReference<T>((T) method.invoke(null, binder));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    callback(closure, serviceReference.get());
                }
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(tag(), "onServiceDisconnected: " + name);
                    serviceReference = null;
                }
            };
            return context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            callback(closure, serviceReference.get());
            return true;
        }
    }

    public void unbindService() {
        Log.i(tag(), "unbindService");
        if (serviceReference != null && serviceReference.get() != null && serviceConnection != null) {
            context.unbindService(serviceConnection);
        }
    }

    private String tag() {
        if (_tag == null) {
            _tag = format("%s<%s>", ServiceMan.class.getSimpleName(), serviceType.getSimpleName());
        }
        return _tag;
    }

    private void callback(final Closure<T> closure, T service) {
        Log.d(tag(), "Calling back: " + closure);
        try {
            closure.with(context, service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface Closure<T> {
        void with(Context context, T service) throws RemoteException;
    }

}
