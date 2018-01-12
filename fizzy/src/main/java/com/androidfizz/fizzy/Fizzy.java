package com.androidfizz.fizzy;

import android.content.Context;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by jitendra.singh on 1/5/2018
 * for RuntimePermission1
 */

public final class Fizzy {

    public interface MultiplePermissionListener {
        void onPermissionsChecked(List<PermissionInfo> list);
    }

    public interface SinglePermissionListener {
        void onPermissionChecked(PermissionInfo permissionInfo);
    }

    private WeakReference<Context> contextWeakReference;
    private static FizzyInstance instance;

    private Fizzy(){}

    public Fizzy(Builder builder) {
        contextWeakReference = new WeakReference<>(builder.context);

        if(builder.singlePermissionListener==null && builder.multiplePermissionListener==null){
            return;
        }

        if (builder.multiplePermissionListener != null) {
            instance = new FizzyInstance(builder.permissions, builder.multiplePermissionListener,
                    builder.debug);
        } else {
            instance = new FizzyInstance(builder.permissions, builder.singlePermissionListener,
                    builder.debug);
        }
    }

    public void request() {
        Context context = contextWeakReference.get();
        if (context != null) {
            instance.startActivity(context);
        }
    }

    static void onActivityStarted(PermissionActivity activity) {
        instance.requestPermissions(activity);
    }

    static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        instance.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public final static class Builder {
        private Context context;
        private SinglePermissionListener singlePermissionListener;
        private MultiplePermissionListener multiplePermissionListener;
        private String[] permissions;
        private boolean debug;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder requiredPermission(SinglePermissionListener singlePermissionListener,
                                          String permission) {
            this.singlePermissionListener = singlePermissionListener;
            this.permissions = new String[]{permission};
            return this;
        }

        public Builder requiredPermission(MultiplePermissionListener multiplePermissionListener,
                                           String... permissions) {
            this.multiplePermissionListener = multiplePermissionListener;
            this.permissions = permissions;
            return this;
        }

        /*public Builder setPermissionListener(PermissionListener listener) {
            this.listener = listener;
            return this;
        }*/

        public Builder debug(boolean enable) {
            this.debug = enable;
            return this;
        }

        public Fizzy build() {
            return new Fizzy(this);
        }
    }
}
