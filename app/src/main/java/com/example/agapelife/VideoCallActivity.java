package com.example.agapelife;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;


public class VideoCallActivity extends AppCompatActivity {
    // Permissions
    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private RtcEngine mRtcEngine;

    public String channelName = "AgapeTest";
    public String TOKEN = "0060ceac686d87c4ba9811edbc7ee666356IABQ2+P/do9meS4NfGlR9dzM54WVMEwFyuaZLu4s2J+0E8sW9ttNBiG1IgCaGCUDEip2YgQAAQAqJnZiAgAqJnZiAwAqJnZiBAAqJnZi";
    public String APP_ID = "0ceac686d87c4ba9811edbc7ee666356";

    // Responsible for handling events such as joining the group and leaving the group
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(VideoCallActivity.this, "Access ID="+uid, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setUpRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            super.onUserOffline(uid, reason);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        ImageView micToggler = findViewById(R.id.btn_audio);
        ImageView videoToggler = findViewById(R.id.btn_video);
        ImageView leaveCall = findViewById(R.id.leave_call);

        micToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAudioMuteClicked(micToggler);
            }
        });

        videoToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onVideoMuteClicked(videoToggler);
            }
        });

        leaveCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    leaveChannel();
                    removeVideo(R.id.local_video_view_container);
                    removeVideo(R.id.remote_video_view_container);

                    Intent intent = new Intent(VideoCallActivity.this, SplashScreenActivity.class);
                    startActivity(intent);
                    finish();
            }
        });

        // Checking for permissions
        if(checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)){
            initAgoraEngineAndJoinChannel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRtcEngine.leaveChannel();
        RtcEngine.destroy();
    }

    public void initAgoraEngineAndJoinChannel(){
        initializeAgoraEngine();

        setUpLocalVideo();
        // Join the channel with a token.
        joinChannelWithUserAccount();
    }

    private void joinChannelWithUserAccount() {
        mRtcEngine.joinChannelWithUserAccount(TOKEN, channelName, "brian_12");
    }

    private void initializeAgoraEngine() {
        //Initializing agora involves initializing the RTCEngine
        try{
            mRtcEngine = RtcEngine.create(getBaseContext(), APP_ID, mRtcEventHandler);
            mRtcEngine.registerLocalUserAccount(APP_ID, "brian_12");
        }
        catch(Exception e){
            Log.e("initializeAgoraError", Log.getStackTraceString(e));

            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
        mRtcEngine.enableVideo();
    }

    private void setUpLocalVideo() {
        Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        FrameLayout localVideoContainer = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        localVideoContainer.addView(surfaceView);
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));
    }

    private void setUpRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FILL, uid));
        surfaceView.setTag(uid);
    }

    private void onRemoteUserLeft() {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        container.removeAllViews();
    }

    public void onAudioMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setImageResource(R.drawable.ic_baseline_mic_24);
        } else {
            btn.setSelected(true);
            btn.setImageResource(R.drawable.ic_baseline_mic_off_24);
        }

        mRtcEngine.muteLocalAudioStream(btn.isSelected());
    }

    public void onVideoMuteClicked(View view) {
        ImageView btn = (ImageView) view;
        if (btn.isSelected()) {
            btn.setSelected(false);
            btn.setImageResource(R.drawable.ic_baseline_videocam_24);
        } else {
            btn.setSelected(true);
            btn.setImageResource(R.drawable.ic_baseline_videocam_off_24);
        }

        mRtcEngine.muteLocalVideoStream(btn.isSelected());

        FrameLayout container = findViewById(R.id.local_video_view_container);
        container.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
        SurfaceView videoSurface = (SurfaceView) container.getChildAt(0);
        videoSurface.setZOrderMediaOverlay(!btn.isSelected());
        videoSurface.setVisibility(btn.isSelected() ? View.GONE : View.VISIBLE);
    }

    public void setUpVideoProfile(){
        mRtcEngine.enableAudio();

    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    private void removeVideo(int containerID) {
        FrameLayout videoContainer = findViewById(containerID);
        videoContainer.removeAllViews();
    }

    public boolean checkSelfPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQ_ID: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("onRequestPermission", "Need permissions " + Manifest.permission.RECORD_AUDIO + "/" + Manifest.permission.CAMERA);
                    break;
                }
                // if permission granted, initialize the engine
                break;
            }
        }
    }

}