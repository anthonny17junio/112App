package com.teltronic.app112.classes.enums

import com.teltronic.app112.classes.enums.IntCodes

enum class PermissionsApp(val code: Int, val manifestName: String) {
    CALL_PHONE(IntCodes.CODE_PERMISSION_CALL_PHONE.code, android.Manifest.permission.CALL_PHONE),
    FINE_LOCATION(IntCodes.CODE_PERMISSION_FINE_LOCATION.code, android.Manifest.permission.ACCESS_FINE_LOCATION);
}