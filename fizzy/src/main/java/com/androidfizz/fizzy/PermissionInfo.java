package com.androidfizz.fizzy;

/**
 * Created by jitendra.singh on 1/5/2018
 * for FizzySample
 */

public class PermissionInfo {
    private String permission;
    private PermissionInfo.Type type;
    public enum Type{
        NOT_GRANTED,
        GRANTED,
        DENIED,
        NEVER_ASK_ME_AGAIN
    }

    public PermissionInfo(String permission, PermissionInfo.Type type) {
        this.permission = permission;
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public PermissionInfo.Type getType() {
        return type;
    }
}
