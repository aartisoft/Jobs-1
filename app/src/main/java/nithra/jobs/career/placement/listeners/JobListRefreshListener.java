package nithra.jobs.career.placement.listeners;

/**
 * Created by arunrk on 17/7/17.
 */

public class JobListRefreshListener {

    public interface OnCustomStateListener {
        void stateChanged();
    }

    private static JobListRefreshListener mInstance;
    private OnCustomStateListener mListener;
    private boolean mState;

    private JobListRefreshListener() {}

    public static JobListRefreshListener getInstance() {
        if(mInstance == null) {
            mInstance = new JobListRefreshListener();
        }
        return mInstance;
    }

    public void setListener(OnCustomStateListener listener) {
        mListener = listener;
    }

    public void changeState(boolean state) {
        if(mListener != null) {
            mState = state;
            notifyStateChange();
        }
    }

    public boolean getState() {
        return mState;
    }

    private void notifyStateChange() {
        mListener.stateChanged();
    }

}