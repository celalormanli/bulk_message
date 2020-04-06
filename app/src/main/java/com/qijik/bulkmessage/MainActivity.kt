package com.qijik.bulkmessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



import android.view.View
import android.widget.Button
import android.widget.ListView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var lv: ListView? = null
    private var modelArrayList: ArrayList<Model>? = null
    private var customAdapter: CustomAdapter? = null
    private var btnselect: Button? = null
    private var btndeselect: Button? = null
    private val animallist = arrayOf("Lion", "Tiger", "Leopard", "Cat","Lion", "Tiger", "Leopard", "Cat","Lion", "Tiger", "Leopard", "Cat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lv = findViewById(R.id.lv) as ListView
        btnselect = findViewById(R.id.select) as Button
        btndeselect = findViewById(R.id.deselect) as Button
        var allSelected:Boolean=false
        modelArrayList = getModel(false)
        customAdapter = CustomAdapter(this, modelArrayList!!)
        lv!!.adapter = customAdapter

        btnselect!!.setOnClickListener {
            if(allSelected==false) {
                modelArrayList = getModel(true)
                allSelected=true
            }
            else{
                modelArrayList = getModel(false)
                allSelected=false
            }
            customAdapter = CustomAdapter(this@MainActivity, modelArrayList!!)
            CustomAdapter.public_modelArrayList = modelArrayList as ArrayList<Model>
            lv!!.adapter = customAdapter
        }

        btndeselect!!.setOnClickListener {
            modelArrayList = getModel(false)
            customAdapter = CustomAdapter(this@MainActivity, modelArrayList!!)
            CustomAdapter.public_modelArrayList = modelArrayList as ArrayList<Model>
            lv!!.adapter = customAdapter
        }
    }

    private fun getModel(isSelect: Boolean): ArrayList<Model> {
        val list = ArrayList<Model>()
        for (i in 0..11) {

            val model = Model()
            model.setSelecteds(isSelect)
            model.setPersons(animallist[i])
            list.add(model)
        }
        return list
    }
}
