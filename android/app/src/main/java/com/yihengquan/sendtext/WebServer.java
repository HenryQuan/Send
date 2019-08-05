package com.yihengquan.sendtext;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static java.nio.charset.StandardCharsets.*;
import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {

    private String message;

    public WebServer(int port, String message) {
        super(port);
        this.message = message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String encode = "";
        try {
            // Working utf-8 encoding
            byte[] kanji = message.getBytes("UTF-8");
            for (byte b : kanji) {
                encode += String.format("\\x%2x", b);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(encode);
    }

}
