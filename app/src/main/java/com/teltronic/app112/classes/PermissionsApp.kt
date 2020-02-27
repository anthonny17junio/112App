package com.teltronic.app112.classes

enum class PermissionsApp(val code: Int, val manifestName: String) {
    CALL_PHONE(100, android.Manifest.permission.CALL_PHONE),
    FINE_LOCATION(200, android.Manifest.permission.ACCESS_FINE_LOCATION);
}