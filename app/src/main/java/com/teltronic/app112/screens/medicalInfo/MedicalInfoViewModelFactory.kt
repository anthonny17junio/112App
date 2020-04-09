package com.teltronic.app112.screens.medicalInfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teltronic.app112.databinding.FragmentMedicalInfoBinding

class MedicalInfoViewModelFactory(
    private val application: Application,
    private val binding: FragmentMedicalInfoBinding
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicalInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicalInfoViewModel(application, binding) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}