package com.yihengquan.send.core

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.UnsupportedEncodingException

/**
 * A simple webserver class that handles responses, now only supports a text message
 */
class WebServer(port: Int) : NanoHTTPD(port) {
    private var _message: String
    var message: String
        get() = _message
        set(value) {
            _message = value
        }

    init {
        // It is empty by default
        _message = ""
    }

    override fun serve(session: IHTTPSession): Response {
        var encode = ""

        try {
            if (message != "") {
                // Working utf-8 encoding
                val kanji = message.toByteArray(charset("UTF-8"))
                for (b in kanji) {
                    encode += String.format("\\x%2x", b)
                }
            }
        } catch (e: UnsupportedEncodingException) {
            Log.i("WebServer", "Encoding is not supported")
            e.printStackTrace()
        }

        return newFixedLengthResponse(encode)
    }

}