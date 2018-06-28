
package nithra.jobs.career.placement.utills;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by NITHRA-1 on 08/04/16.
 */

public class L {
    public static void t(Context context, String message){
        if(message == null) message = "";
        if (context != null) Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
