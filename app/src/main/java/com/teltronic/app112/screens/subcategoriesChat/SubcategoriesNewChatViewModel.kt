package com.teltronic.app112.screens.subcategoriesChat

import androidx.lifecycle.ViewModel
import com.teltronic.app112.classes.Category
import com.teltronic.app112.classes.Subcategories
import com.teltronic.app112.classes.Subcategory

class SubcategoriesNewChatViewModel(category: Category) : ViewModel() {
    private var listSubcategories: List<Subcategory>?

    init {
        listSubcategories = Subcategories(category).listSubcategories
    }
}