package sandwitch.isafelife.services;

import android.app.IntentService;
import android.content.Intent;

// TODO: Rasmus lav den som jeg har
public class AcceleratorBackgroundService extends IntentService {

    public AcceleratorBackgroundService(){
        super("AcceleratorBackgroundService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AcceleratorBackgroundService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
