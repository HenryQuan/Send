package com.yihengquan.sendtext

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.UnsupportedEncodingException

/**
 * A simple webserver class that handles responses, now only supports a text message
 */
class WebServer(port: Int) : NanoHTTPD(port) {
    var message: String
        get() = message
        set(value) {
            // Only update if the new message is meaningful
            if (!value.isNullOrBlank()) message = value
        }

    override fun serve(session: IHTTPSession): Response {
        var encode = ""

        try {
            // Working utf-8 encoding
            val kanji = message.toByteArray(charset("UTF-8"))
            for (b in kanji) {
                encode += String.format("\\x%2x", b)
            }
        } catch (e: UnsupportedEncodingException) {
            Log.i("WebServer", "Encoding is not supported")
            e.printStackTrace()
        }

        return newFixedLengthResponse(encode)
    }

}