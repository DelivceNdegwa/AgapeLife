package com.example.agapelife.networking.services;


import android.net.Uri;
import android.os.Build;
import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class AgapeSocketsListener{
    String SOCKET_URL;
    String message;
    private WebSocketClient mWebSocketClient;

    public AgapeSocketsListener(String SOCKET_URL) {
        this.SOCKET_URL = SOCKET_URL;
    }

    public void connectWebSocket(){
        URI uri;
        try {
            uri = new URI(SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Websocket", "Opened");
                JSONObject socketMessage = new JSONObject();
                try {
                    socketMessage.put("message", "Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                    mWebSocketClient.send(String.valueOf(socketMessage));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMessage(String s) {
                message = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tvServerMessage.setText(tvServerMessage.getText().toString()+"\n\n"+ message);
//                    }
//                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public String getMessage() {
        return message;
    }
}
