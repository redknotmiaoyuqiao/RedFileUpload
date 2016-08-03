package com.redknot.uploadfile;

import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by redknot on 8/3/16.
 */
public class UploadSession {

    private int block_size;
    private File file;
    private String url;
    private String name;
    private UploadListener uploadListener = null;

    public UploadSession(String url, File file, String name, int block_size) {
        this.url = url;
        this.file = file;
        this.block_size = block_size;
        this.name = name;
    }

    public void setListener(UploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public void start() {
        if (this.uploadListener == null) {
            Log.e("error", "there are no listener");
            return;
        } else {
            UploadThread t = new UploadThread();
            new Thread(t).start();
        }
    }

    private class UploadThread implements Runnable {
        @Override
        public void run() {
            //String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/abc.mp4";

            //File file = new File(savePath);

            long chunks = (file.length() / block_size) + 1;
            long chunk = 0;

            InputStream is = null;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            byte[] b = new byte[block_size];
            int hashRead = 0;

            try {
                while ((hashRead = is.read(b)) > 0) {
                    String tmp_url = url;
                    tmp_url = tmp_url + "?name=" + name + "&chunks=" + chunks + "&chunk=" + chunk;

                    byte[] buff = new byte[hashRead];

                    for (int i = 0; i < buff.length; i++) {
                        buff[i] = b[i];
                    }

                    String result = HttpUpload.uploadFile(buff, tmp_url);

                    Message msg = new Message();
                    if (result == null) {
                        break;
                    } else {
                        String url = null;
                        try {
                            JSONObject jo = new JSONObject(result);
                            url = jo.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (url == null) {
                            //上传中
                            msg.what = 1;
                            msg.obj = (int) (chunk / (chunks + 0.0f) * 100);
                        } else {
                            //上传完毕
                            msg.what = 2;
                            msg.obj = url;
                        }
                    }
                    uploadListener.sendMessage(msg);

                    chunk++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
