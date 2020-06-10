package com.qijik.bulkmessage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlin.system.exitProcess

class Cover : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.main_background))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getSupportActionBar()?.hide();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        }
        else{
            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
            }, 1000)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
        if (grantResults[0]==PackageManager.PERMISSION_DENIED)
        {
            moveTaskToBack(true);
            exitProcess(-1)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}

