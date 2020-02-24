package com.teltronic.app112.screens.subcategoriesChat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teltronic.app112.classes.Category
import com.teltronic.app112.classes.Subcategories
import com.teltronic.app112.classes.Subcategory

class SubcategoriesNewChatViewModel(category: Category) : ViewModel() {
    private var _listSubcategories = MutableLiveData<List<Subcategory>>()
    val listSubcategories: LiveData<List<Subcategory>>
        get() = _listSubcategories

    private var _boolListInitialSet = MutableLiveData<Boolean>()
    val boolListInitialSet: LiveData<Boolean>
        get() = _boolListInitialSet

    init {
        _listSubcategories.value = Subcategories(category).listSubcategories
        _boolListInitialSet.value = false
    }

    fun changeListInitBool() {
        _boolListInitialSet.value = true
    }
}