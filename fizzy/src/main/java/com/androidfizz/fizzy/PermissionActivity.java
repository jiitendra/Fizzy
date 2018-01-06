package com.androidfizz.fizzy;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;

/**
 * Created by jitendra.singh on 1/5/2018
 * for RuntimePermission1
 */

public class PermissionActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fizzy.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Fizzy.onActivityStarted(this);
    }
}
