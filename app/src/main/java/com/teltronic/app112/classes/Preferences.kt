package com.teltronic.app112.classes

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import com.teltronic.app112.R
import java.util.*


object Preferences {
    private const val idKeyLanguage = R.string.KEY_LANGUAGE
    private const val idNameSettingsPreferences = R.string.name_settings_preferences

    fun setLocate(lang: String, context: Context) {
        val keyLanguage = context.resources.getString(idKeyLanguage)
        val nameSettingsPreferences = context.resources.getString(idNameSettingsPreferences)

        val locale = Locale(lang)

        Locale.setDefault(locale)

        val config = Configuration()

        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        val editor =
            context.getSharedPreferences(nameSettingsPreferences, Context.MODE_PRIVATE).edit()
        editor.putString(keyLanguage, lang)
        editor.apply()
    }

    fun loadLocate(context: Context) {
        val keyLanguage = context.resources.getString(idKeyLanguage)
        val nameSettingsPreferences = context.resources.getString(idNameSettingsPreferences)
        val sharedPreferences =
            context.getSharedPreferences(nameSettingsPreferences, Activity.MODE_PRIVATE)
        val language = sharedPreferences.getString(keyLanguage, "")
        setLocate(language!!, context)
    }

}