package com.yihengquan.sendtext

import fi.iki.elonen.NanoHTTPD
import java.io.UnsupportedEncodingException

class WebServer(port: Int, private var message: String) : NanoHTTPD(port) {
    fun setMessage(msg: String) {
        message = msg
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
            e.printStackTrace()
        }
        return newFixedLengthResponse(encode)
    }

}