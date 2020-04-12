package com.qijik.bulkmessage

import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract


import android.view.View
import android.widget.Button
import android.widget.ListView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }
    var listOfContacs= arrayListOf<Contact>()
    val nameSurnames= mutableListOf<String>()
    val phoneNumbers = mutableListOf<String>()
    private var lv: ListView? = null
    private var modelArrayList: ArrayList<Model>? = null
    private var customAdapter: CustomAdapter? = null
    private var btnselect: Button? = null
    private var btndeselect: Button? = null
    private val animallist = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val intent = Intent(this, SendMessage::class.java)
//        startActivity(intent)
        loadContacts()
        var a = listOfContacs.distinctBy { it.phoneNumber }
        var listOfContacs = a.distinctBy { it.nameSurname }
        for (contact in listOfContacs) {

            System.out.println(contact.nameSurname+' '+contact.phoneNumber.replace(" ",""))
            nameSurnames.add(contact.nameSurname.toString())
            phoneNumbers.add(contact.phoneNumber.toString().replace(" ",""))
            animallist.add(contact.nameSurname.toString())
        }

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
        for (i in 0..animallist.size-1) {

            val model = Model()
            model.setSelecteds(isSelect)
            model.setPersons(animallist[i])
            list.add(model)
        }
        return list
    }
    private fun loadContacts(): StringBuilder {
        var builder = StringBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS),
                SendMessage.PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {
            builder = getContacts()
            // print(builder)
            return builder
        }
        return builder
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == SendMessage.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }
    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                        if (cursorPhone != null) {
                            if(cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(
                                        cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    builder.append("Contact: ").append(name).append(", Phone Number: ").append(
                                        phoneNumValue).append("\n\n")
                                    var contact=Contact()
                                    contact.nameSurname=name
                                    contact.phoneNumber=phoneNumValue
                                    listOfContacs.add(contact)
//                                    Log.e("Name ===>",phoneNumValue);
                                }
                            }
                        }
                        if (cursorPhone != null) {
                            cursorPhone.close()
                        }
                    }
                }
            } else {
                //   toast("No contacts available!")
            }
        }
        if (cursor != null) {
            cursor.close()
        }
        return builder
    }
}
