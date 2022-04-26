package com.example.agapelife;

import android.os.Bundle;

import com.example.agapelife.databinding.ActivityUserMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import tech.gusavila92.websocketclient.WebSocketClient;

public class UserMainActivity extends AppCompatActivity {

    private ActivityUserMainBinding binding;
    private WebSocketClient webSocketClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view_user);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_user_home, R.id.navigation_user_appointments, R.id.navigation_user_records)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewUser, navController);


//        createWebSocketClient();
    }

//    private void createWebSocketClient() {
//        URI uri;
//        try {
//            // Connect to local host
//            uri = new URI("ws://10.0.2.2:8080/websocket");
//        }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//        webSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//                Log.i("WebSocket", "Session is starting");
//                webSocketClient.send("Hello World!");
//            }
//
//            @Override
//            public void onTextReceived(String s) {
//                Log.i("WebSocket", "Message received");
//                final String message = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
////                            TextView textView = findViewById(R.id.animalSound);
////                            textView.setText(message);
//                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onBinaryReceived(byte[] data) {
//            }
//
//            @Override
//            public void onPingReceived(byte[] data) {
//            }
//
//            @Override
//            public void onPongReceived(byte[] data) {
//            }
//
//            @Override
//            public void onException(Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public void onCloseReceived() {
//                Log.i("WebSocket", "Closed ");
//                Toast.makeText(MainActivity.this, "onClose received", Toast.LENGTH_SHORT).show();
//            }
//        };
//        webSocketClient.setConnectTimeout(10000);
//        webSocketClient.setReadTimeout(60000);
//        webSocketClient.enableAutomaticReconnection(5000);
//        webSocketClient.connect();
//
//        }

}