# Fizzy

Fizzy, for android run-time permissions. It's a simplest approach to request and handle android run-time permissions. The objective of this library is to provide a clean architecture for requesting runtime permissions and remove boiler plate code form your activity. 

------
### Demo

![Screenshot](screenshot.gif)

-----

### Dependency

For including library into your ``build.gradle``

```groovy
dependencies{
    compile 'com.androidfizz.fizzy:fizzy:1.0'
}
```

### Requesting Single Permission

To start using this library you need to create an ``Fizzy`` instance by using ``Fizzy.Builder`` with valid activity instance.

```java
    new Fizzy
        .Builder(this)
        .requiredPermission(this,
             Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .build()
        .request();
```

For single permission, register a ``Fizzy.SinglePermissionListener`` with ``requiredPermission`` method and implement ``onPermissionChecked`` method to handle result. And there you can check is permission granted, denied, or set to never ask me again, and perform your appropiate action.

```java

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

```

### Requesting Multiple Permissions

```java
        new Fizzy
                .Builder(this)
                .requiredPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.GET_ACCOUNTS)
                .build()
                .request();
```


Similarly to single permissions, you need to register a ``Fizzy.MultiplePermissionListener`` with ``requiredPermission`` method and implement ``onPermissionChecked`` method to handle result.

```java
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
```

### Enable logging

To set log enable make a call using Builder's ``debug(true)`` method. By default a logging is set to false.

```java
        new Fizzy
                .Builder(this)
                .requiredPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .debug(true)
                .build()
                .request();
```


