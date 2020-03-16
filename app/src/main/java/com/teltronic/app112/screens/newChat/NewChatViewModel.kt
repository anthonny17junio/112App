package com.teltronic.app112.screens.newChat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teltronic.app112.classes.enums.Category

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
    private var _categoryNavigate = MutableLiveData<Category>()
    val categoryNavigate: LiveData<Category>
        get() = _categoryNavigate

    private var _boolNavigateToConfirmChat = MutableLiveData<Boolean>()
    val boolNavigateToConfirmChat: LiveData<Boolean>
        get() = _boolNavigateToConfirmChat

    //Make call
    private var _boolMakeCall = MutableLiveData<Boolean>()
    val boolMakeCall: LiveData<Boolean>
        get() = _boolMakeCall

    init {
        _categoryNavigate.value = null
        _boolNavigateToConfirmChat.value = false

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

    fun navigateCategoryComplete() {
        _categoryNavigate.value = null
    }

    fun navigateToCategory(category: Category) {
        _categoryNavigate.value = category
    }

    fun navigateToConfirmChat() {
        _boolNavigateToConfirmChat.value = true
    }

    fun navigateToConfirmChatComplete() {
        _boolNavigateToConfirmChat.value = false
    }

    //Make call
    fun makeCall() {
        _boolMakeCall.value = true
    }

    fun makeBoolCallComplete() {
        _boolMakeCall.value = false
    }

}