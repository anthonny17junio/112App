package com.teltronic.app112.classes

enum class PermissionsApp(val code: Int, val manifestName: String) {
    CALL_PHONE(Codes.CODE_PERMISSION_CALL_PHONE.code, android.Manifest.permission.CALL_PHONE),
    FINE_LOCATION(Codes.CODE_PERMISSION_FINE_LOCATION.code, android.Manifest.permission.ACCESS_FINE_LOCATION);
}