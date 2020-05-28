package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.teltronic.app112.R
import com.teltronic.app112.classes.enums.Subcategory

class SubcategoriesListAdapter(
    private var application: Application,
    private var subcategories: List<Subcategory>
) : BaseAdapter() {

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return ViewHolder.from(convertView, position, application, subcategories)
    }


    private class ViewHolder private constructor(row: View) {
        var lblTxtSubcategory: TextView = row.findViewById(R.id.lbl_name)
        var iconSubcategory: ImageView = row.findViewById(R.id.icon_subcategory)

        companion object {
            fun from(
                convertView: View?,
                position: Int,
                application: Application,
                subcategories: List<Subcategory>
            ): View {
                val viewHolder: ViewHolder
                val view: View

                if (convertView == null) {
                    val inflater =
                        application.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    view = inflater.inflate(R.layout.item_subcategory, null)
                    viewHolder = ViewHolder(view)

                    view.tag = viewHolder
                } else {
                    view = convertView
                    viewHolder = view.tag as ViewHolder
                }

                val subcat = subcategories[position]
                viewHolder.lblTxtSubcategory.text = application.resources.getString(subcat.idTitle)
                viewHolder.iconSubcategory.setImageResource(subcat.idIcon!!)

                return view
            }
        }

    }

    override fun getItem(position: Int): Subcategory {
        return subcategories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return subcategories.size
    }


}
