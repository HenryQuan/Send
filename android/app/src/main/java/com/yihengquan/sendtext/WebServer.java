package com.yihengquan.sendtext;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {

    private String message;

    public WebServer(int port, String message) {
        super(port);
        this.message = message;
    }

    public void shutdown() {
        this.closeAllConnections();
        this.stop();
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    @Override
    public Response serve(IHTTPSession session) {
        System.out.println(message);
        return newFixedLengthResponse(message);
    }

}
