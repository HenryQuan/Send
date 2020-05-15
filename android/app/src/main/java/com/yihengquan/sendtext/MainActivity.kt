package com.yihengquan.sendtext

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
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
    private val server = WebServer(port)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        try {
            // Start server
            server.start(2000)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("MainActivity", "Timeout or server is already running")
        }

        // Focus inputBox
        val inputBox = findViewById<TextView>(R.id.inputText)
        if (inputBox.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        // Check if wifi is connected
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = cm.activeNetworkInfo
        if (wifi != null) {
            // Get device's IP address
            val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            var ip = numberToIPAddress(wm.connectionInfo.ipAddress)
            if (ip != null) {
                ip = ip.replace("/", "") + ':' + port
                val ipLabel = findViewById<TextView>(R.id.idLabel)
                ipLabel.text = ip
            } else {
                // Lock everything
                showAlertDialog(
                        "IP address is not valid",
                        "This usually shouldn't happen.\nPlease try again later.",
                        cancelable = false,
                        closeApp = false
                )
            }
        } else {
            // Hotspot might work but I don't know
            showAlertDialog(
                    "Not connected",
                    "Please connect to the network",
                    cancelable = false,
                    closeApp = true
            )
        }
    }

    private fun showAlertDialog(title: String, message: String, cancelable: Boolean = true, closeApp: Boolean = false) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable) // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    if (closeApp) finish()
                    else null // this just closes the dialog
                }
                .show()
    }

    fun sendText(v: View?) {
        val input: EditText = findViewById<EditText>(R.id.inputText)
        val msg = input.text.toString()
        server.message = msg
    }

    /**
     * Convert ip address to a readable string
     * From https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code/54417079#54417079
     * @param hostAddress
     * @return
     */
    private fun numberToIPAddress(hostAddress: Int): String? {
        val addressBytes = byteArrayOf((0xff and hostAddress).toByte(),
                (0xff and (hostAddress shr 8)).toByte(),
                (0xff and (hostAddress shr 16)).toByte(),
                (0xff and (hostAddress shr 24)).toByte())

        // Return "" if the host is not valid, this shouldn't happen
        return try {
            InetAddress.getByAddress(addressBytes).toString()
        } catch (e: UnknownHostException) {
            Log.i("MainActivity", "Host is unknown")
            e.printStackTrace()
            null
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
       return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        server.stop()
        super.onDestroy()
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