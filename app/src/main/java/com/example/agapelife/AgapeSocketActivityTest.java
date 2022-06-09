package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.networking.services.AgapeSocketsListener;
import com.example.agapelife.networking.services.URLs;
import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class AgapeSocketActivityTest extends AppCompatActivity {
    Button btnLoadServer;
    TextView tvServerMessage;

    private AgapeSocketsListener agapeSocketsListener;

    private final String SOCKET_URL = URLs.SOCKET_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agape_socket_test);

        btnLoadServer = findViewById(R.id.btn_load_server);
        tvServerMessage = findViewById(R.id.tv_server_message);


        btnLoadServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectWebSocket();
            }
        });
    }

    private void start() {
        Request request = new Request.Builder().url(SOCKET_URL).build();
    }

    private void connectWebSocket() {
        agapeSocketsListener = new AgapeSocketsListener(SOCKET_URL);
        runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvServerMessage.setText(tvServerMessage.getText().toString()+"\n\n"+ agapeSocketsListener.getMessage());
                    }
                });
    }


}