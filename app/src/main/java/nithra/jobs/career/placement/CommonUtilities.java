package nithra.jobs.career.placement;

import android.content.Context;
import android.content.Intent;

import nithra.jobs.career.placement.utills.SU;
import nithra.jobs.career.placement.utills.U;


public final class CommonUtilities {

    // give your server registration url here
    static final String SERVER_URL = SU.GCM_REGISTER;

    // Google project id
    static final String SENDER_ID = "22073589686";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "Nithra Jobs GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "nithra.jobs.career.placement.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

}
