package com.redknot.uploadfile;

import android.os.Handler;
import android.os.Message;

/**
 * Created by redknot on 8/3/16.
 */
public abstract class UploadListener extends Handler {

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1) {
            onProgress((int) msg.obj);
        }
        if (msg.what == 2) {
            onFinish((String) msg.obj);
        }
    }

    public abstract void onProgress(int progress);

    public abstract void onFinish(String url);
}
