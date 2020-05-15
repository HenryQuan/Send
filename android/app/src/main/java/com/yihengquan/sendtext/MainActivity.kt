package com.yihengquan.sendtext

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.IOException
import java.net.InetAddress
import java.net.UnknownHostException

class MainActivity : AppCompatActivity() {
    private val port = 9587
    private val server = WebServer(port, "")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        try {
            // Start server
            server.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Focus inputBox
        val inputBox = findViewById<TextView>(R.id.inputText)
        if (inputBox.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        // Check if wifi is connected
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (wifi.isConnected) {
            // Get device's IP address
            val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            var ip = intToInetAddress(wm.connectionInfo.ipAddress).toString()
            ip = ip.replace("/", "") + ':' + port
            val ipLabel = findViewById<TextView>(R.id.idLabel)
            ipLabel.text = ip
        } else {
            // Hotspot might work but I don't know
            AlertDialog.Builder(this)
                    .setTitle("WiFi is not connected")
                    .setMessage("Please connect to the same network first")
                    .setCancelable(false) // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.ok) { dialog, which -> finish() }
                    .show()
        }
    }

    fun sendText(v: View?) {
        val input = findViewById<EditText>(R.id.inputText)
        val msg = input.text.toString()
        println(msg)
        server.setMessage(msg)
    }

    /**
     * Convert ip address to a readable string
     * From https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code/54417079#54417079
     * @param hostAddress
     * @return
     */
    private fun intToInetAddress(hostAddress: Int): InetAddress {
        val addressBytes = byteArrayOf((0xff and hostAddress).toByte(),
                (0xff and (hostAddress shr 8)).toByte(),
                (0xff and (hostAddress shr 16)).toByte(),
                (0xff and (hostAddress shr 24)).toByte())
        return try {
            InetAddress.getByAddress(addressBytes)
        } catch (e: UnknownHostException) {
            throw AssertionError()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }

    fun checkLocalhost(item: MenuItem?) {
        val localhost = Intent(Intent.ACTION_VIEW, Uri.parse("http://127.0.0.1:$port"))
        startActivity(localhost)
    }

    fun gotoGitHub(item: MenuItem?) {
        val github = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/HenryQuan/SendText"))
        startActivity(github)
    }
}