package com.teltronic.app112.screens.subcategoriesChat

import android.app.Application
import android.widget.ListAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.teltronic.app112.adapters.SubcategoriesListAdapter
import com.teltronic.app112.classes.enums.Category
import com.teltronic.app112.classes.Subcategories
import com.teltronic.app112.classes.enums.Subcategory

class SubcategoriesNewChatViewModel(category: Category, application: Application) : AndroidViewModel(application) {
    private var _listSubcategories = MutableLiveData<List<Subcategory>>()

    private var _subcategoryNavigate = MutableLiveData<Subcategory>()
    val subcategoryNavigate: LiveData<Subcategory>
        get() = _subcategoryNavigate

    private var _listAdapter = MutableLiveData<ListAdapter>()
    val listAdapter: LiveData<ListAdapter>
        get() = _listAdapter


    init {
        _listSubcategories.value = Subcategories(category).listSubcategories
        _subcategoryNavigate.value = null
        //Inicializo el adaptador para el list view de subcategorías
        _listAdapter.value = SubcategoriesListAdapter(getApplication(), _listSubcategories.value!!)
    }

    fun navigateSubcategoryComplete() {
        _subcategoryNavigate.value = null
    }

    fun navigateToSubcategory(subcategory: Subcategory) {
        _subcategoryNavigate.value = subcategory
    }
}