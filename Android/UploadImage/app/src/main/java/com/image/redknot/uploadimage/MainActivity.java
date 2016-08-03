package com.image.redknot.uploadimage;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.redknot.uploadfile.UploadListener;
import com.redknot.uploadfile.UploadSession;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        show = (TextView) findViewById(R.id.show);

        /*
        用之前记住添加网络权限和文件读取等权限
         */

        String url = "http://192.168.1.105/niu/api.php/Home/Image/upload";//上传地址

        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/googlechrome.dmg";
        File file = new File(savePath);//要上传的文件的File

        String name = "googlechrome.dmg";//文件标识,最好是文件md5加后缀

        int block_size = 1024 * 1024;//每个上传块的大小,这里是1mb,不要超过2mb

        UploadSession session = new UploadSession(url, file, name, block_size);
        session.setListener(new MyListener());
        session.start();//开始上传
    }

    //添加上传监听
    private class MyListener extends UploadListener {

        @Override
        public void onProgress(int progress) {
            Log.e("callback", progress + "%");
            show.setText(progress + "%");
        }

        @Override
        public void onFinish(String url) {
            Log.e("callback", url + "");
        }
    }
}
