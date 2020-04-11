package com.qijik.bulkmessage

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Message : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        SendMessage("TEST","0000000000")
    }
    private fun SendMessage(message: String, number: String) { // Toast.makeText(this, "Telefon dili değişmiş", Toast.LENGTH_SHORT).show();
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Mesaj Gönder")
        builder.setMessage("$message #$number# numaralı kişiye gönderilsin mi?")
        builder.setCancelable(false)
        builder.setPositiveButton("YES",
            DialogInterface.OnClickListener { dialogInterface, i ->
                try {
                    val smgr: SmsManager = SmsManager.getDefault()
                    smgr.sendTextMessage(number, null, message, null, null)
                } catch (ee: Exception) {
                    Toast.makeText(this, "Mesaj Gönderilemedi.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        builder.setNegativeButton("NO",
            DialogInterface.OnClickListener { dialogInterface, i ->
                Toast.makeText(
                    this,
                    "Mesaj Gönderilmedi.",
                    Toast.LENGTH_SHORT
                ).show()
            })
        builder.show()
    }
}
