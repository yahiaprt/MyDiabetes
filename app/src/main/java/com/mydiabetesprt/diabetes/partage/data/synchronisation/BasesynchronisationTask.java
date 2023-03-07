package com.mydiabetesprt.diabetes.partage.data.synchronisation;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public abstract class BasesynchronisationTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    private WeakReference<Context> context;
    private OnAsyncProgressListener<Result> onAsyncProgressListener;

    public BasesynchronisationTask(Context context, OnAsyncProgressListener<Result> onAsyncProgressListener) {
        this.context = new WeakReference<>(context);
        this.onAsyncProgressListener = onAsyncProgressListener;
    }

    protected Context getContext() {
        return context.get();
    }

    @Override
    protected void onPostExecute(Result data) {
        if (onAsyncProgressListener != null) {
            onAsyncProgressListener.onPostExecute(data);
        }
    }

    public interface OnAsyncProgressListener <Result> {
        void onPostExecute(Result result);
    }
}