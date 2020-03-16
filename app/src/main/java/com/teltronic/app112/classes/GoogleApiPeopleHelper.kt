package com.teltronic.app112.classes

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.AsyncTask
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.PeopleServiceScopes
import com.google.api.services.people.v1.model.Person
import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.StringCodes
import com.teltronic.app112.screens.MainActivity
import com.teltronic.app112.screens.userProfile.UserProfileViewModel
import java.text.DateFormatSymbols

object GoogleApiPeopleHelper {

    //People API
    private lateinit var mGoogleApiClient: GoogleApiClient

    private const val appName = "112App"
    private val httpTransport = NetHttpTransport()
    private val jsonFactory = JacksonFactory.getDefaultInstance()
    private const val redirectUrl = "urn:ietf:wg:oauth:2.0:oob"

    fun initGoogleApiClient(activity: MainActivity) { //Solicita autenticación con cuenta de google
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(StringCodes.PEOPLE_API_CLIENT_ID.code)
            .requestScopes(
                Scope(PeopleServiceScopes.USER_BIRTHDAY_READ),
                Scope(PeopleServiceScopes.USERINFO_PROFILE)
            )
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(activity as Context)
            .enableAutoManage(activity as FragmentActivity, activity)
            .addOnConnectionFailedListener(activity)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .build()

        mGoogleApiClient.connect()
    }

    //Fun google authentication (cuando se inicia en la aplicación)
    //Desde MainActivity llega un Activity
    fun googleAuth(resultCode: Int, activity: Activity) {
        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(intent, resultCode)
    }

    //Desde UserProfileViewModel llega un fragment
    fun googleAuth(resultCode: Int, activity: Fragment) {
            val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            activity.startActivityForResult(intent, resultCode)
    }

    //Obtiene el servicio de People Api PeopleAPI
    private fun getPeopleService(serverAuthCode: String): PeopleService {
        // Exchange auth code for access token
        val tokenResponse = GoogleAuthorizationCodeTokenRequest(
            httpTransport,
            jsonFactory,
            StringCodes.PEOPLE_API_CLIENT_ID.code,
            StringCodes.PEOPLE_API_CLIENT_SECRET.code,
            serverAuthCode,
            redirectUrl
        ).execute()

        // Then, create a GoogleCredential object using the tokens from GoogleTokenResponse
        val credential = GoogleCredential.Builder()
            .setClientSecrets(
                StringCodes.PEOPLE_API_CLIENT_ID.code,
                StringCodes.PEOPLE_API_CLIENT_SECRET.code
            )
            .setTransport(httpTransport)
            .setJsonFactory(jsonFactory)
            .build()

        credential.setFromTokenResponse(tokenResponse)

        // credential can then be used to access Google services
        return PeopleService.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName(appName)
            .build()
    }

    class PeopleAsyncTask(private val viewModel: UserProfileViewModel, private val res: Resources) :
        AsyncTask<String, Void, Person>() { //AuthCode es el primer String
        override fun doInBackground(vararg params: String?): Person {
            val serverAuthCode = params[0]!!
            val peopleService = getPeopleService(serverAuthCode)
            return peopleService.people()
                .get("people/me")
                .setPersonFields("names,photos,birthdays,genders,userDefined")
                .execute()
        }

        override fun onPostExecute(person: Person?) {
            super.onPostExecute(person)
            getGender(viewModel, res, person)
            getBirthDate(viewModel, person)
        }

    }

    private fun getGender(viewModel: UserProfileViewModel, res: Resources, person: Person?) {
        when (person?.genders?.get(0)?.value) {
            "male" ->
                viewModel.setGender(res.getString(R.string.gender_male))
            "female" ->
                viewModel.setGender(res.getString(R.string.gender_female))
            "other" ->
                viewModel.setGender(res.getString(R.string.gender_other))
            "unknown" ->
                viewModel.setGender(res.getString(R.string.gender_unknown))
        }
    }

    private fun getBirthDate(viewModel: UserProfileViewModel, person: Person?) {
        var day = -1
        var month = -1
        var year = -1

        val birthdays = person?.birthdays
        if (birthdays != null) {
            for (birthday in birthdays) {
                if (year == -1) {
                    day = birthday.date.day
                    month = birthday.date.month
                    if (birthday.date.year != null)
                        year = birthday.date.year
                }
            }
        }

        if (day > -1) {
            viewModel.setBirthDay(day.toString())
        }

        if (month > -1) {
            val strMonth = DateFormatSymbols().months[month - 1]
            viewModel.setBirthMonth(strMonth)
        }

        if (year > -1) {
            viewModel.setBirthYear(year.toString())
        }
    }

}