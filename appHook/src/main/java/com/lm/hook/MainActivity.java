package com.lm.hook;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lm.hook.utils.HookUtils;
import com.lm.hook.utils.RecordLogUtils;
import com.meitu.secret.SigEntity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        requestPermission();
        android.util.Log.e("sliver", "getSignatures before ");
        String value = getSignatures();
        android.util.Log.e("sliver", "getSignatures: "+value);
        isPmsHook();
//        scanFiles();
    }

    private void initViews() {
        final EditText editText = findViewById(R.id.pkg_input);
        Button addPkg = findViewById(R.id.add_pkg);
        addPkg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pkg = editText.getText().toString();
                if (TextUtils.isEmpty(pkg)) {
                    Toast.makeText(MainActivity.this, "包名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                XposedHookImpl.getInstance().addHookTarget(pkg);
                editText.setText("");
                Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        XposedHookImpl.getInstance().init();
    }

    private void requestPermission() {
        int value = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (value != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                              @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }
    }

    public String getSignatures() {
//        PackageManager pm = getPackageManager();
//        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
//        Iterator<PackageInfo> it = apps.iterator();
//        while(it.hasNext()){
//            PackageInfo info = it.next();
//            if(info.packageName.equals("com.campmobile.snowcamera")){
//                return info.signatures[0].toCharsString();
//            }
//        }
//        return null;
        String value1 = "material/get_rank.json";
        String[] values2 = new String[] {
              "1", "2540419324", "26" , "sagit", "xm64", "MI 6", "2", "sagit", "Xiaomi", "9940", "1089867608", "b2:b2:0c:08:60:f0",
                "zh-Hans", "3", "CN", "40227506935615980627", "1", "android", "1", "137477942098568", "8.0.0","e0e46235d9ff8e8c",
                "1", "WIFI", "",
        };
        String value3 = "10003";

        Log.e("sliver", "packageName: "+getPackageName());

        return "test";
    }

    private void isPmsHook() {
        HookUtils.init(getClassLoader());
    }

    private void scanFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dataRoot = getDataDir();
                File androidDataRoot = getExternalCacheDir().getParentFile();
                try {
                    Log.e("sliver111", "dataRoot: "+dataRoot.getAbsolutePath());
                    Log.e("sliver111", "androidDataRoot: "+androidDataRoot.getAbsolutePath());
                    recordFile(null, dataRoot);
                    recordFile(null, androidDataRoot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void recordFile(RandomAccessFile raf, File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for(File f: files) {
                recordFile(raf,f);
            }
        }else {
            String value = file.length()+"\t"+file.getAbsolutePath()+"\n";
//            raf.writeBytes(value));
            Log.e("sliver", value);
        }
    }

}
