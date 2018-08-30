package com.joeh.peoplewithfriends;


import android.support.test.espresso.IdlingResource;

import okhttp3.Dispatcher;

/** An IdlingResource that waits for our OkHttpClient to quiesce. */
public class OkHttpIdlingResource implements IdlingResource {

    private Dispatcher mDispatcher;
    private volatile ResourceCallback mCallback;

    /**
     * OkHttpIdlingResource constructor
     * Uses the dispatcher from our OkHttpClient.
     */
    public OkHttpIdlingResource()
    {
        mDispatcher = RetrofitClient.getInstance().getOkHttpClient().dispatcher();
        mDispatcher.setIdleCallback(new Runnable() {
            @Override
            public void run() {
                if( mCallback != null )
                {
                    mCallback.onTransitionToIdle();
                }
            }
        });
    }
    /**
     * Returns the name of the resources (used for logging and idempotency  of registration).
     */
    @Override
    public String getName() {
        return "OkHttpIdlingResource";
    }

    /**
     * Returns {@code true} if resource is currently idle. Espresso will <b>always</b> call this
     * method from the main thread, therefore it should be non-blocking and return immediately.
     */
    @Override
    public boolean isIdleNow() {
        boolean idle = mDispatcher.runningCallsCount() == 0 && mDispatcher.queuedCallsCount() == 0;
        if( idle && mCallback != null )
        {
            mCallback.onTransitionToIdle();
        }
        return idle;
    }

    /**
     * Registers the given {@link ResourceCallback} with the resource. Espresso will call this method:
     * <ul>
     * <li>with its implementation of {@link ResourceCallback} so it can be notified asynchronously
     * that your resource is idle
     * <li>from the main thread, but you are free to execute the callback's onTransitionToIdle from
     * any thread
     * <li>once (when it is initially given a reference to your IdlingResource)
     * </ul>
     * <br>
     * You only need to call this upon transition from busy to idle - if the resource is already idle
     * when the method is called invoking the call back is optional and has no significant impact.
     *
     * @param callback
     */
    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

}
