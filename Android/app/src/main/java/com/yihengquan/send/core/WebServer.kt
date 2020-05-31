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
        // If message doesn't include "content://com", try it like a string
        if (message.contains("content://com")) {
            val stream: InputStream? = context.contentResolver.openInputStream(Uri.parse(message))
            val res = newChunkedResponse(
                Response.Status.OK,
                getContentType(message),
                stream
            )

            if (message.contains("raw%3A")) {
                // Only add this for raw files
                res.addHeader("Content-Disposition", getFileName(message))
            }

            return res
        } else {
            var encode = ""
            try {
                if (message != "") {
                    // Working utf-8 encoding
                    val kanji = message.toByteArray(charset("UTF-8"))
                    for (b in kanji) {
                        encode += String.format("\\x%2x", b)
                    }
                }
                Log.i("WebServer", encode)
            } catch (e: UnsupportedEncodingException) {
                Log.i("WebServer", "Encoding is not supported")
                e.printStackTrace()
            }

            return newFixedLengthResponse(encode)
        }
    }

    private fun getFileName(uri: String): String {
        return "attachment; filename=\"" + uri.split("%2F").last() + "\""
    }

    private fun getContentType(uri: String): String {
        return when {
            uri.contains("image%3A") -> "image/png img/jpeg img/gif"
            uri.contains("video%3A") -> "video/mp4 video/quicktime video/webm video/mpeg"
            uri.contains("audio%3A") -> "audio/mpeg audio/x-wav "
            uri.contains("raw%3A") -> "application/octet-stream"
            else -> ""
        }
    }

}