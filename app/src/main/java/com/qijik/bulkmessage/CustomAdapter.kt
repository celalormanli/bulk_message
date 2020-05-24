package com.qijik.bulkmessage
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import java.util.ArrayList

class CustomAdapter(private val context: Context, modelArrayList: ArrayList<Model>) : BaseAdapter() {

    private var modelArrayList: ArrayList<Model>

    init {
        this.modelArrayList = modelArrayList
    }

    override fun getViewTypeCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {

        return position
    }

    fun getModelArrayLisst(): ArrayList<Model> {
        return modelArrayList
    }

    override fun getCount(): Int {
        return modelArrayList.size
    }

    override fun getItem(position: Int): Any {
        return modelArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.lv_item, null, true)

            holder.checkBox = convertView!!.findViewById(R.id.cb) as CheckBox
            holder.nameSurname = convertView!!.findViewById(R.id.nameSurname) as TextView
            holder.phoneNumber=convertView!!.findViewById(R.id.phoneNumber) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.nameSurname!!.setText(modelArrayList[position].getPersons())
        holder.phoneNumber!!.setText(modelArrayList[position].getNumbers())
        holder.checkBox!!.isChecked = modelArrayList[position].getSelecteds()
        holder.checkBox!!.setTag(R.integer.btnplusview, convertView)
        holder.checkBox!!.tag = position
        holder.checkBox!!.setOnClickListener {
            val tempview = holder.checkBox!!.getTag(R.integer.btnplusview) as View
            val nameS = tempview.findViewById(R.id.nameSurname) as TextView
            val pos = holder.checkBox!!.tag as Int
            Toast.makeText(context, modelArrayList[position].getPersons().toString(), Toast.LENGTH_SHORT).show()

            if (modelArrayList[pos].getSelecteds()) {
                modelArrayList[pos].setSelecteds(false)
                public_modelArrayList = modelArrayList
            } else {
                modelArrayList[pos].setSelecteds(true)
                public_modelArrayList = modelArrayList
            }
            for (model in modelArrayList)
            {
                println(model.getPersons())
                println(model.getNumbers())
                println(model.isSelected)
            }
        }
        return convertView
    }
    private inner class ViewHolder {
        var checkBox: CheckBox? = null
        var nameSurname: TextView? = null
        var phoneNumber:TextView?=null
    }
    companion object {
        lateinit var public_modelArrayList: ArrayList<Model>
    }
}