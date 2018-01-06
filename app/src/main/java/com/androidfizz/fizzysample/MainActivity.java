package com.androidfizz.fizzysample;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.androidfizz.fizzy.Fizzy;
import com.androidfizz.fizzy.PermissionInfo;

import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, Fizzy
        .SinglePermissionListener, Fizzy.MultiplePermissionListener {

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnRequest).setOnClickListener(this);
        tvResult=(TextView)findViewById(R.id.tvResult);
    }

    @Override
    public void onClick(View view) {
        requestPermission();
    }

    /*
    //for multiple permissions
    private void requestPermission() {
        new Fizzy
                .Builder(this)
                .requiredPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.GET_ACCOUNTS)
                .debug(true)
                .build()
                .request();
    }*/

    //for single permission
    private void requestPermission() {
        new Fizzy
                .Builder(this)
                .requiredPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .debug(true)
                .build()
                .request();
    }

    @Override
    public void onPermissionChecked(PermissionInfo permissionInfo) {
        String permission=permissionInfo.getPermission();
        switch (permissionInfo.getType()){
            case GRANTED:
                tvResult.setText(String.format(Locale.US, "%s Granted", permission));
                break;
            case DENIED:
                tvResult.setText(String.format(Locale.US, "%s Denied", permission));
                break;
            case NEVER_ASK_ME_AGAIN:
                tvResult.setText(String.format(Locale.US, "%s Never ask me again", permission));
                break;
        }
    }

    @Override
    public void onPermissionsChecked(List<PermissionInfo> list) {
        StringBuilder builder=new StringBuilder();
        int i=1;
        for(PermissionInfo permissionInfo: list){
            builder.append(String.format(Locale.US, "%d. ", i++));
            String permission=permissionInfo.getPermission();
            switch (permissionInfo.getType()){
                case GRANTED:
                    builder.append(String.format(Locale.US, "%s Granted", permission));
                    break;
                case DENIED:
                    builder.append(String.format(Locale.US, "%s Denied", permission));
                    break;
                case NEVER_ASK_ME_AGAIN:
                    builder.append(String.format(Locale.US, "%s Never ask me again", permission));
                    break;
            }
            builder.append("\n");
        }
        tvResult.setText(builder.toString());
    }
}
