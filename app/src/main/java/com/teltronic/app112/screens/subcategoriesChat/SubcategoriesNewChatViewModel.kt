package com.teltronic.app112.screens.subcategoriesChat

import android.app.Activity
import android.widget.ListAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teltronic.app112.adapters.SubcategoriesListAdapter
import com.teltronic.app112.classes.Category
import com.teltronic.app112.classes.Subcategories
import com.teltronic.app112.classes.Subcategory

class SubcategoriesNewChatViewModel(category: Category, activity: Activity) : ViewModel() {
    private var _listSubcategories = MutableLiveData<List<Subcategory>>()

    private var _listAdapter = MutableLiveData<ListAdapter>()
    val listAdapter: LiveData<ListAdapter>
        get() = _listAdapter

    private var _boolNavigateToConfirmChat = MutableLiveData<Boolean>()
    val boolNavigateToConfirmChat: LiveData<Boolean>
        get() = _boolNavigateToConfirmChat

    init {
        _listSubcategories.value = Subcategories(category).listSubcategories
        _boolNavigateToConfirmChat.value = false
        //Inicializo el adaptador para el list view de subcategorías
        _listAdapter.value = SubcategoriesListAdapter(activity, _listSubcategories.value!!)
    }

    fun navigateToConfirmChat() {
        _boolNavigateToConfirmChat.value = true
    }

    fun navigateToConfirmChatComplete() {
        _boolNavigateToConfirmChat.value = false
    }
}