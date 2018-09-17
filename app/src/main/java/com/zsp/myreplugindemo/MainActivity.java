package com.zsp.myreplugindemo;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.FileNotFoundException;
import java.io.IOException;

import rx.Observer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button=findViewById(R.id.btn);
        Button button1=findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(MainActivity.this).
                        request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).
                        subscribe(WRITE_EXTERNAL_STORAGE_Observer);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RePlugin.createIntent("com.zsp.myplugin", "com.zsp.myplugin.MainActivity");
                intent.putExtra("zsp","host传过来的数据");
                if (!RePlugin.startActivity(MainActivity.this, intent)) {
                    Toast.makeText(MainActivity.this, "启动失败,请先安装插件", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 提示权限
     */
    private Observer<Boolean> WRITE_EXTERNAL_STORAGE_Observer = new Observer<Boolean>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Boolean o) {
            if (o) {
                PluginInfo pluginInfo =  RePlugin.install(Environment.getExternalStorageDirectory().toString()+"/plugin.apk");
                if (pluginInfo==null){
                    Toast.makeText(MainActivity.this,"插件已安装",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this,"插件信息："+pluginInfo.getJSON(),Toast.LENGTH_LONG).show();
                }
            } else {
            }
        }
    };

}
