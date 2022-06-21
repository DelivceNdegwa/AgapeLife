package com.example.agapelife;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agapelife.models.OnlineDoctor;
import com.example.agapelife.networking.services.URLs;
//import com.google.gson.JsonObject;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

public class AgapeSocketActivityTest extends AppCompatActivity {
    Button btnLoadServer;
    TextView tvServerMessage;
    String message;
    private WebSocketClient mWebSocketClient;
    private final String SOCKET_URL = URLs.ONLINE_DOCTORS;
    List<OnlineDoctor> onlineDoctorList = new ArrayList<>();


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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("Websocket_msg", message);
                        fetchData(message);

                    }
                });
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

    private void fetchData(String message) {
        try {
            JSONObject listJson = new JSONObject(message);
            JSONArray doctorsArray = listJson.getJSONArray("doctor_list");

            for(int i=0; i < doctorsArray.length(); i++){
                JSONObject doctorObject = doctorsArray.getJSONObject(i);
                addDoctor(doctorObject);

            }

            Log.i("Websocket_list", String.valueOf(onlineDoctorList));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addDoctor(JSONObject doctorObject) {
        OnlineDoctor doctor = null;
        try {
            doctor = new OnlineDoctor(
                    doctorObject.getLong("id"),
                    doctorObject.getLong("id_number"),
                    doctorObject.getLong("phone_number"),
                    doctorObject.getBoolean("is_verified"),
                    doctorObject.getBoolean("is_available"),
                    doctorObject.getString("profile_image"),
                    doctorObject.getString("first_name"),
                    doctorObject.getString("last_name"),
                    doctorObject.getString("hospital"),
                    doctorObject.getString("specialization")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        onlineDoctorList.add(doctor);
        Log.i("Websocket_list_obj", String.valueOf(doctor.getProfileImage()));
    }


}