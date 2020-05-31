package com.yihengquan.send.core

import android.content.Context
import android.net.Uri
import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException


/**
 * A simple webserver class that handles responses, now only supports a text message
 */
class WebServer(port: Int, private val context: Context) : NanoHTTPD(port) {
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
        // Message can be



        var encode = ""

        try {
//            if (message != "") {
//                // Working utf-8 encoding
//                val kanji = message.toByteArray(charset("UTF-8"))
//                for (b in kanji) {
//                    encode += String.format("\\x%2x", b)
//                }
//            }
            val stream: InputStream? = context.contentResolver.openInputStream(Uri.parse(message))
            val res = newChunkedResponse(
                Response.Status.OK,
                "",
                stream
            )

            return res
//            encode = reader.readText()
            Log.i("WebServer", encode)
        } catch (e: UnsupportedEncodingException) {
            Log.i("WebServer", "Encoding is not supported")
            e.printStackTrace()
        }

        val res = newFixedLengthResponse(encode)
        res.addHeader("Content-Type", "image/png image/jpeg")
        return res
    }


}