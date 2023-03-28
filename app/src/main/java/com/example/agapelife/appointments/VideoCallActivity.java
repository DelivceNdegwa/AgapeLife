package com.example.agapelife.appointments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.agapelife.R;
import com.example.agapelife.UserMainActivity;
import com.example.agapelife.medical_reports.DoctorNotesActivity;
import com.example.agapelife.utils.PreferenceStorage;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


public class VideoCallActivity extends AppCompatActivity {
    public String channelName="Toothache";
    public String TOKEN="006f320179c8f6947c29eb4a6e48f2a0cadIABJIctFeGSb6SD2sPwejAKu/+nt1rt/Yc7w29Po279JJET4OP7j4um9IgB8zQcDGQzgYgQAAQCZyN5iAgCZyN5iAwCZyN5iBACZyN5i";
    public String APP_ID = "f320179c8f6947c29eb4a6e48f2a0cad";

    private long appointmentId;

    ImageView micToggler, videoToggler, leaveCall;

    private RtcEngine mRtcEngine;
    PreferenceStorage preferenceStorage;

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
                    setupRemoteVideo(uid);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        micToggler = findViewById(R.id.btn_audio);
        videoToggler = findViewById(R.id.btn_video);
        leaveCall = findViewById(R.id.leave_call);

        preferenceStorage = new PreferenceStorage(this);
        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            initializeAndJoinChannel();
        }

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


    }

    private void initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), APP_ID, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }
        // For a live streaming scenario, set the channel profile as BROADCASTING.
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        // Set the client role as BORADCASTER or AUDIENCE according to the scenario.
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine.enableVideo();
        FrameLayout container = findViewById(R.id.local_video_view_container);
        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));

        // Join the channel with a token.
//        mRtcEngine.joinChannel(TOKEN, channelName, "", 0);
        mRtcEngine.joinChannelWithUserAccount(TOKEN, channelName, "delivce_mwangi");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        appointmentId = intent.getLongExtra("APPOINTMENT_ID", 0);
        channelName = "Test_appointment";//intent.getStringExtra("CHANNEL_NAME");
        TOKEN = "006f320179c8f6947c29eb4a6e48f2a0cadIADyaOtmUm6Ct5rbnOzAY0ra3UfzGX5t8oX+rjRbAr8eaIc4ebYAAAAAEAD2FHLceFCxYgEAAQB4ULFi";
//        intent.getStringExtra("TOKEN_ID");

        leaveCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leaveChannel();
                removeVideo(R.id.local_video_view_container);
                removeVideo(R.id.remote_video_view_container);

                Intent intent;
                if(preferenceStorage.isDoctor()){
                    intent = new Intent(VideoCallActivity.this, DoctorNotesActivity.class);
                    intent.putExtra("APPOINTMENT_ID", appointmentId);
                }
                else{
                    intent = new Intent(VideoCallActivity.this, UserMainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));
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

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    private void removeVideo(int containerID) {
        FrameLayout videoContainer = findViewById(containerID);
        videoContainer.removeAllViews();
    }

}