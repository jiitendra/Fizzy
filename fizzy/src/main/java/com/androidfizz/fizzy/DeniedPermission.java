package com.androidfizz.fizzy;

/**
 * Created by jitendra.singh on 1/5/2018
 * for RuntimePermission1
 */

public final class DeniedPermission {
    private String permission;
    private Type type;
    public enum Type{
        DENIED,
        NEVER_ASK_ME_AGAIN
    }

    DeniedPermission(String permission, Type type) {
        this.permission = permission;
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "DeniedPermission{" +
                "permission='" + permission + '\'' +
                ", type=" + type +
                '}';
    }
}
