package com.elicius.timr;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TimrIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String START_TIMR = "com.elicius.timr.action.START_TIMR";

    // TODO: Rename parameters
    private static final String SECONDS = "com.elicius.timr.extra.SECONDS";
    private static final String MINUTES = "com.elicius.timr.extra.MINUTES";
    private static final String HOURS = "com.elicius.timr.extra.HOURS";

    private TimerThread timerThread;
    private static TimerActivity.TimerResultReceiver receiver;

    public TimrIntentService() {
        super("TimrIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startTimer(Context context, Timer timer, TimerActivity.TimerResultReceiver resultreceiver) {
        Intent intent = new Intent(context, TimrIntentService.class);
        intent.setAction(START_TIMR);
        intent.putExtra(SECONDS, timer.getSeconds());
        intent.putExtra(MINUTES, timer.getMinutes());
        intent.putExtra(HOURS, timer.getHours());

        receiver = resultreceiver;

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (START_TIMR.equals(action)) {
                final int seconds = intent.getIntExtra(SECONDS, 0);
                final int minutes = intent.getIntExtra(MINUTES, 0);
                final int hours = intent.getIntExtra(HOURS, 0);
                handleActionFoo(seconds, minutes, hours);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(int seconds, int minutes, int hours) {
        timerThread = new TimerThread(seconds, minutes, hours, receiver);
        timerThread.start();
    }
}
