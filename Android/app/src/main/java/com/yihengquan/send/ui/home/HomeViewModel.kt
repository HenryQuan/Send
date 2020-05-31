package com.yihengquan.send.ui.home

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.net.InetAddress
import java.net.UnknownHostException

class HomeViewModel : ViewModel() {

    private val port = "9587"

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> = _address

    fun setIPAddress(context: Context?) {
        context ?: return
        val wm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        var ip = numberToIPAddress(wm?.connectionInfo?.ipAddress)
        ip?.let {
            _address.value = it.replace("/", "") + ':' + port
        }
    }

    /**
     * Convert ip address to a readable string
     * From https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code/54417079#54417079
     * @param hostAddress
     * @return
     */
    private fun numberToIPAddress(hostAddress: Int?): String? {
        hostAddress ?: return null

        val addressBytes = byteArrayOf((0xff and hostAddress).toByte(),
            (0xff and (hostAddress shr 8)).toByte(),
            (0xff and (hostAddress shr 16)).toByte(),
            (0xff and (hostAddress shr 24)).toByte())

        // Return "" if the host is not valid, this shouldn't happen
        return try {
            InetAddress.getByAddress(addressBytes).toString()
        } catch (e: UnknownHostException) {
            Log.i("HomeViewModel", "Host is unknown")
            e.printStackTrace()
            null
        }
    }

}