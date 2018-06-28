package nithra.jobs.career.placement.listeners;

/**
 * Created by arunrk on 17/7/17.
 */

public class RefreshListener {

    private static RefreshListener mInstance;
    private OnCustomStateListener mListener;
    private boolean mState;

    public interface OnCustomStateListener {
        void stateChanged();
    }

    private RefreshListener() {}

    public static RefreshListener getInstance() {
        if(mInstance == null) {
            mInstance = new RefreshListener();
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