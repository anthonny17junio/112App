package com.teltronic.app112.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.teltronic.app112.R
import com.teltronic.app112.classes.Subcategory

class SubcategoriesListAdapter(
    private var activity: Activity,
    private var subcategories: List<Subcategory>
) : BaseAdapter() {

    private class ViewHolder(row: View) {
        var lblTxtSubcategory: TextView = row.findViewById(R.id.lbl_name)
        var iconSubcategory: ImageView = row.findViewById(R.id.icon_subcategory)
    }


    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater =
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.item_subcategory, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val subcat = subcategories[position]
        viewHolder.lblTxtSubcategory.text = activity.resources.getString(subcat.idTitle)
        viewHolder.iconSubcategory.setImageResource(subcat.idIcon)

        return view
    }

    override fun getItem(position: Int): Any {
        return subcategories[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return subcategories.size
    }


}