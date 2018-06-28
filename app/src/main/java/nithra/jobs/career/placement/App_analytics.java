package nithra.jobs.career.placement;

/**
 * Created by NITHRA on 17/10/2015.
 */

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;



import nithra.jobs.career.placement.R;


public class App_analytics extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}