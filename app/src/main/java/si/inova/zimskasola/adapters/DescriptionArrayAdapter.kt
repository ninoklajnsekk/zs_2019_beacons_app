package si.inova.zimskasola.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.zimskasola.R

import si.inova.zimskasola.data.Stuff

class DescriptionArrayAdapter(context: Context, resource: Int, objects: List<Stuff?>)
    :ArrayAdapter<Stuff?>(context,resource,objects){

    val stuffList = objects
    val currContext = context



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("hejho","")
        var view: View? = convertView
        if( view == null)
            view = LayoutInflater.from(currContext).inflate(R.layout.description_items_item_list,null)

        val stuffItem = stuffList[position]

        val tv_title: TextView = view!!.findViewById(R.id.tv_title)
        val tv_subtitle: TextView = view.findViewById(R.id.tv_subtitle)
        val tv_type: TextView = view.findViewById(R.id.tv_type)
        val iv_type_icon: ImageView = view.findViewById(R.id.iv_typeIcon)

        tv_title.text = stuffItem?.category
        tv_subtitle.text = stuffItem?.name
        tv_type.text = stuffItem?.description

        Glide.with(view).load(stuffItem?.icon).into(iv_type_icon)

        return view

    }
}