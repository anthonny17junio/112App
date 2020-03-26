package com.teltronic.app112.classes.enums

enum class PermissionsApp(val code: Int, val manifestName: String) {
    CALL_PHONE(IntCodes.CODE_PERMISSION_CALL_PHONE.code, android.Manifest.permission.CALL_PHONE),
    FINE_LOCATION_FROM_MAIN_FRAGMENT(IntCodes.CODE_PERMISSION_FINE_LOCATION_FROM_MAIN_FRAGMENT.code, android.Manifest.permission.ACCESS_FINE_LOCATION),
    FINE_LOCATION_FROM_CONFIRM_MESSAGE_FRAGMENT(IntCodes.CODE_PERMISSION_FINE_LOCATION_FROM_CONFIRM_FRAGMENT.code, android.Manifest.permission.ACCESS_FINE_LOCATION);
}