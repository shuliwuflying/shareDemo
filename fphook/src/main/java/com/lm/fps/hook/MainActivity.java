package com.lm.fps.hook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        requestPermission();
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
}
