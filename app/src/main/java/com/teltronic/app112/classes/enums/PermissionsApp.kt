package com.teltronic.app112.classes.enums

enum class PermissionsApp(val code: Int, val manifestName: String) {
    CALL_PHONE(IntCodes.CODE_PERMISSION_CALL_PHONE.code, android.Manifest.permission.CALL_PHONE),
    CAMERA(IntCodes.CODE_PERMISSION_CAMERA.code, android.Manifest.permission.CAMERA),
    READ_EXTERNAL_STORAGE(IntCodes.CODE_PERMISSION_READ_EXTERNAL_STORAGE.code,android.Manifest.permission.READ_EXTERNAL_STORAGE),
    WRITE_EXTERNAL_STORAGE(IntCodes.CODE_PERMISSION_WRITE_EXTERNAL_STORAGE.code,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
    FINE_LOCATION_FROM_MAIN_FRAGMENT_TO_LOCATION(IntCodes.CODE_PERMISSION_FINE_LOCATION_FROM_MAIN_FRAGMENT_TO_LOCATION.code, android.Manifest.permission.ACCESS_FINE_LOCATION),
    FINE_LOCATION_FROM_MAIN_FRAGMENT_TO_NEW_CHAT(IntCodes.CODE_PERMISSION_FINE_LOCATION_FROM_MAIN_FRAGMENT_TO_NEW_CHAT.code, android.Manifest.permission.ACCESS_FINE_LOCATION),
    FINE_LOCATION_FROM_MAIN_ACTIVITY_TO_CONFIGURATION(IntCodes.CODE_PERMISSION_FINE_LOCATION_FROM_MAIN_ACTIVITY_TO_CONFIGURATION.code, android.Manifest.permission.ACCESS_FINE_LOCATION),
    FINE_LOCATION_FROM_CONFIRM_MESSAGE_FRAGMENT(IntCodes.CODE_PERMISSION_FINE_LOCATION_FROM_CONFIRM_FRAGMENT.code, android.Manifest.permission.ACCESS_FINE_LOCATION)
}