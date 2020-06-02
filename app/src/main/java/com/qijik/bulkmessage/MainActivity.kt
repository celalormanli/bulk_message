package com.qijik.bulkmessage
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*
import kotlin.system.exitProcess

//TODO:permission sorunu çözülecek
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
    private var btnSelect: Button? = null
    private var btnSend: Button? = null
    private var btnName: Button? = null
    private var btnNameSurname: Button? = null
    private var messageText:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.main_background))
        loadContacts()
        var listOfContacs = listOfContacs.distinctBy { Pair(it.phoneNumber, it.phoneNumber) }
        listOfContacs = listOfContacs.sortedWith(compareBy({ it.nameSurname }, { it.nameSurname }))
        for (contact in listOfContacs) {
            System.out.println(contact.nameSurname+' '+contact.phoneNumber.replace(" ",""))
            nameSurnames.add(contact.nameSurname.toString())
            phoneNumbers.add(contact.phoneNumber.toString().replace(" ",""))
        }



        lv = findViewById(R.id.lv) as ListView
        btnSelect = findViewById(R.id.btnSelect) as Button
        btnSend = findViewById(R.id.btnSend) as Button
        btnName=findViewById(R.id.btnName) as Button
        btnNameSurname=findViewById(R.id.btnNameSurname) as Button
        messageText=findViewById(R.id.messageText) as EditText
        modelArrayList?.sortWith(compareBy { it.person })

        var allSelected:Boolean=false
        modelArrayList = getModel(false)
        customAdapter = CustomAdapter(this, modelArrayList!!)
        lv!!.adapter = customAdapter
        btnSelect!!.setOnClickListener {
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

        btnSend!!.setOnClickListener {
            modelArrayList = customAdapter!!.getModelArrayLisst()
            for (i in 0..modelArrayList!!.size-1){
               // println(modelArrayList!![i].getPersons().split(' '))
             //   println(modelArrayList!![i].getNumbers())
             //   println(modelArrayList!![i].getSelecteds())
                if(modelArrayList!![i].getSelecteds()==true) {
                    var message: String = messageText?.text.toString()
                    if(message.length>0){
                    message =message.replace(getString(R.string.name_surname_txt), modelArrayList!![i].getPersons().toString())
                    message = message.replace(getString(R.string.name_txt),modelArrayList!![i].getPersons().split(' ')[0].toString()
                    )
                    println(message)
                    // println("________________________")
//                val smsManager = SmsManager.getDefault() as SmsManager
//                smsManager.sendTextMessage("05075972188", null, "sms message", null, null)
                    }
                }
            }
        }
        btnName!!.setOnClickListener {
            println("HERE")
            var cursorPosition = messageText!!.getSelectionStart();
            println(cursorPosition.toString())
            var tmpText= messageText!!.text.toString()
            tmpText = tmpText.substring(0, cursorPosition) + getString(R.string.name_txt).toString() + tmpText.substring(cursorPosition, tmpText.length)
            messageText!!.setText(tmpText.toString())
            messageText!!.setSelection(cursorPosition+getString(R.string.name_txt).toString().length)
        }
        btnNameSurname!!.setOnClickListener {
            var cursorPosition = messageText!!.getSelectionStart();
            println(cursorPosition.toString())
            var tmpText= messageText!!.text.toString()
            tmpText = tmpText.substring(0, cursorPosition) + getString(R.string.name_surname_txt).toString() + tmpText.substring(cursorPosition, tmpText.length)
            messageText!!.setText(tmpText.toString())
            messageText!!.setSelection(cursorPosition+getString(R.string.name_surname_txt).toString().length)
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true);
        exitProcess(-1)
        super.onBackPressed()
    }

    private fun getModel(isSelect: Boolean): ArrayList<Model> {
        val list = ArrayList<Model>()
        for (i in 0..nameSurnames.size-1) {
            val model = Model()
            model.setSelecteds(isSelect)
            model.setPersons(nameSurnames[i])
            model.setNumbers(phoneNumbers[i])
            list.add(model)
        }
        return list
    }
    private fun loadContacts(): StringBuilder {
        var builder = StringBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS),
                MainActivity.PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            builder = getContacts()
            return builder
        }
        return builder
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == MainActivity.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            }
        }
    }
    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
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
                                    contact.phoneNumber=phoneNumValue.replace(" ","")
                                    listOfContacs.add(contact)
                                }
                            }
                        }
                        if (cursorPhone != null) {
                            cursorPhone.close()
                        }
                    }
                }
            }
        }
        if (cursor != null) {
            cursor.close()
        }
        return builder
    }
}