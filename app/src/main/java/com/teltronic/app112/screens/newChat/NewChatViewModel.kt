package com.teltronic.app112.screens.newChat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teltronic.app112.classes.Category

class NewChatViewModel : ViewModel() {

    //Crime
    private var _idCrimeIcon = MutableLiveData<Int>()
    val idCrimeIcon: LiveData<Int>
        get() = _idCrimeIcon

    private var _idTxtCrime = MutableLiveData<Int>()

    //Accident
    private var _idAccidentIcon = MutableLiveData<Int>()
    val idAccidentIcon: LiveData<Int>
        get() = _idAccidentIcon

    private var _idTxtAccident = MutableLiveData<Int>()

    //Medical Urgency
    private var _idMedicalUrgencyIcon = MutableLiveData<Int>()
    val idMedicalUrgencyIcon: LiveData<Int>
        get() = _idMedicalUrgencyIcon

    private var _idTxtMedicalUrgency = MutableLiveData<Int>()

    //Other
    private var _idOtherIcon = MutableLiveData<Int>()
    val idOtherIcon: LiveData<Int>
        get() = _idOtherIcon

    private var _idTxtOther = MutableLiveData<Int>()
    val idTxtOther: LiveData<Int>
        get() = _idTxtOther

    //Navigations
    private var _boolNavigateToCrimeSubcategory = MutableLiveData<Boolean>()
    val boolNavigateToCrimeSubcategory: LiveData<Boolean>
        get() = _boolNavigateToCrimeSubcategory

    private var _boolNavigateToAccidentSubcategory = MutableLiveData<Boolean>()
    val boolNavigateToAccidentSubcategory: LiveData<Boolean>
        get() = _boolNavigateToAccidentSubcategory

    private var _boolNavigateToMedicalUrgencySubcategory = MutableLiveData<Boolean>()
    val boolNavigateToMedicalUrgencySubcategory: LiveData<Boolean>
        get() = _boolNavigateToMedicalUrgencySubcategory

    private var _boolNavigateToConfirmChat = MutableLiveData<Boolean>()
    val boolNavigateToConfirmChat: LiveData<Boolean>
        get() = _boolNavigateToConfirmChat

    //Make call
    private var _boolMakeCall = MutableLiveData<Boolean>()
    val boolMakeCall: LiveData<Boolean>
        get() = _boolMakeCall

    init {
        _boolNavigateToConfirmChat.value = false
        _boolNavigateToCrimeSubcategory.value = false
        _boolNavigateToAccidentSubcategory.value = false
        _boolNavigateToMedicalUrgencySubcategory.value = false



        _idTxtCrime.value = Category.CRIME.idTitle
        _idCrimeIcon.value = Category.CRIME.idIcon

        _idTxtAccident.value = Category.ACCIDENT.idTitle
        _idAccidentIcon.value = Category.ACCIDENT.idIcon

        _idTxtMedicalUrgency.value = Category.MEDICAL_URGENCY.idTitle
        _idMedicalUrgencyIcon.value = Category.MEDICAL_URGENCY.idIcon

        _idTxtOther.value = Category.OTHER.idTitle
        _idOtherIcon.value = Category.OTHER.idIcon

        _boolMakeCall.value = false

    }

    fun navigateToConfirmChat() {
        _boolNavigateToConfirmChat.value = true
    }

    fun navigateToConfirmChatComplete() {
        _boolNavigateToConfirmChat.value = false
    }

    fun navigateToCrimeSubcategory() {
        _boolNavigateToCrimeSubcategory.value = true
    }

    fun navigateToCrimeSubcategoryComplete() {
        _boolNavigateToCrimeSubcategory.value = false
    }

    fun navigateToAccidentSubcategory() {
        _boolNavigateToAccidentSubcategory.value = true
    }

    fun navigateToAccidentSubcategoryComplete() {
        _boolNavigateToAccidentSubcategory.value = false
    }

    fun navigateToMedicalUrgencySubcategory() {
        _boolNavigateToMedicalUrgencySubcategory.value = true
    }

    fun navigateToMedicalUrgencySubcategoryComplete() {
        _boolNavigateToMedicalUrgencySubcategory.value = false
    }

    //Make call
    fun makeCall() {
        _boolMakeCall.value = true
    }

    fun makeBoolCallComplete() {
        _boolMakeCall.value = false
    }

}