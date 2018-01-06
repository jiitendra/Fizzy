package com.androidfizz.fizzy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jitendra.singh on 1/5/2018
 * for RuntimePermission1
 */

final class FizzyInstance {

    private static final String TAG = "Fizzy";
    private static final int REQUEST_CODE_PERMISSIONS = 1088;
    private String[] permissions;
    private HashMap<String, PermissionInfo.Type> map = new HashMap<>();
    private Fizzy.SinglePermissionListener singlePermissionListener;
    private Fizzy.MultiplePermissionListener multiplePermissionListener;
    private boolean debug;
    private WeakReference<Activity> activityWeakReference;
    private boolean isForMultiplePermissions;

    FizzyInstance(String[] permissions,
                  Fizzy.SinglePermissionListener singlePermissionListener, boolean debug) {
        this.permissions = permissions;
        this.singlePermissionListener = singlePermissionListener;
        this.debug = debug;
    }

    FizzyInstance(String[] permissions,
                  Fizzy.MultiplePermissionListener multiplePermissionListener, boolean debug) {
        this.permissions = permissions;
        this.multiplePermissionListener = multiplePermissionListener;
        this.debug = debug;
        this.isForMultiplePermissions = true;
    }


    void startActivity(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!isPermissionsAlreadyGranted(context)) {
                context.startActivity(new Intent(context, PermissionActivity.class));
            } else {
                permissionsChecked(prepareList());
            }
        } else {
            permissionsChecked(prepareList());
        }
    }

    private boolean isPermissionsAlreadyGranted(Context context) {
        if (permissions != null && permissions.length > 0) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String permission : permissions) {
                int hasPermission = ContextCompat.checkSelfPermission(context, permission);
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    map.put(permission, PermissionInfo.Type.NOT_GRANTED);
                    listPermissionsNeeded.add(permission);
                } else {
                    map.put(permission, PermissionInfo.Type.GRANTED);
                }
            }

            if (listPermissionsNeeded.size() > 0) {
                this.permissions = listPermissionsNeeded
                        .toArray(new String[listPermissionsNeeded.size()]);
                return false;
            }
        } else {
            if (debug) {
                Log.i(TAG, "No permission requested, use \"requiredPermissions\"");
            }
        }
        return true;
    }


    private List<PermissionInfo> prepareList() {
        List<PermissionInfo> list=new ArrayList<>(map.size());
        for (Map.Entry<String, PermissionInfo.Type> entry : map.entrySet()) {
            list.add(new PermissionInfo(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    private void permissionsChecked(List<PermissionInfo> list) {
        if(list!=null && list.size()>0) {
            if (isForMultiplePermissions) {
                if (multiplePermissionListener != null) {
                    multiplePermissionListener.onPermissionsChecked(list);
                } else if (debug) {
                    Log.i(TAG, "Permission denied");
                    Log.i(TAG, "Permission listener is null, use \"setPermissionListener\"");
                }
            } else {
                if (singlePermissionListener != null) {
                    singlePermissionListener.onPermissionChecked(list.get(0));
                } else if (debug) {
                    Log.i(TAG, "Permission denied");
                    Log.i(TAG, "Permission listener is null, use \"setPermissionListener\"");
                }
            }
        }
    }

    public void requestPermissions(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
        if (permissions != null && permissions.length > 0) {
            if (debug) {
                Log.i(TAG, "Requesting permission");
                for (int i = 0; i < permissions.length; i++) {
                    Log.i(TAG, String.format(Locale.US, "%d. %s", i, permissions[i]));
                }
            }
            ActivityCompat.requestPermissions(activity, this.permissions,
                    REQUEST_CODE_PERMISSIONS);
        }
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0) {
                Map<String, Integer> perms = new HashMap<>();
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }


                Activity activity = activityWeakReference.get();
                if (activity == null) {
                    return;
                }

                //List<PermissionInfo> list = new ArrayList<>(map.size());
                for (Map.Entry<String, Integer> entry : perms.entrySet()) {
                    String permission = entry.getKey();
                    if (entry.getValue() != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                permission)) {
                            map.put(permission, PermissionInfo.Type.DENIED);
                            //list.add(new PermissionInfo(permission, PermissionInfo.Type.DENIED));
                        } else {
                            map.put(permission, PermissionInfo.Type.NEVER_ASK_ME_AGAIN);
                            //list.add(new PermissionInfo(permission,PermissionInfo.Type
                            // .NEVER_ASK_ME_AGAIN));
                        }
                    } else {
                        //list.add(new PermissionInfo(permission, PermissionInfo.Type.GRANTED));
                        map.put(permission, PermissionInfo.Type.GRANTED);
                    }
                }
                permissionsChecked(prepareList());
            }
        }
    }
}
