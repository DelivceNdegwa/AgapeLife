package com.example.agapelife.appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.AgoraTokenGenerator;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentJoiningRoomActivity extends AppCompatActivity {
    String strAppointmentTitle, strAppointmentAbout, strUserName, strAppointmentDay, strAppointmentTime;
    TextView tvAppointmentTitle, tvAppointmentAbout,tvAppointmentTime, tvAppointmentDate, tvHostName, tvUserName;
    ImageView btnViewDoctor, btnViewPatientProfile;
    Button btnJoinAppointment;
    PreferenceStorage preferenceStorage;
    
    long appointmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_joining_room);

        preferenceStorage = new PreferenceStorage(this);

        tvAppointmentTitle = findViewById(R.id.tv_appointment_title);
        tvAppointmentAbout = findViewById(R.id.tv_about_text);
        tvHostName = findViewById(R.id.tv_patient_request_name);
        tvUserName = findViewById(R.id.tv_patient_name);
        tvAppointmentTime = findViewById(R.id.tv_aptmt_time);
        tvAppointmentDate = findViewById(R.id.tv_aptmt_day);

        btnViewDoctor = findViewById(R.id.iv_view_doctor);
        btnViewPatientProfile = findViewById(R.id.iv_view_patient);
        
        btnJoinAppointment = findViewById(R.id.btn_join_appointment);

        btnViewDoctor.setVisibility(View.GONE);
        
        
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        strAppointmentTitle = i.getStringExtra("APPOINTMENT_TITLE");
        strAppointmentAbout = i.getStringExtra("APPOINTMENT_ABOUT");
        strUserName = i.getStringExtra("FULL_NAME");
        appointmentId = i.getLongExtra("APPOINTMENT_ID", 0);
        strAppointmentDay = i.getStringExtra("APPOINTMENT_DATE");
        strAppointmentTime = i.getStringExtra("APPOINTMENT_TIME");

        tvAppointmentTitle.setText(strAppointmentTitle);
        tvAppointmentAbout.setText(strAppointmentAbout);
        tvAppointmentDate.setText(strAppointmentDay);
        tvAppointmentTime.setText(strAppointmentTime);


        if(preferenceStorage.isDoctor()){
            tvHostName.setText("You");
            tvUserName.setText(strUserName);
        }
        else{
            tvHostName.setText(strUserName);
            tvUserName.setText("You");
        }


        btnJoinAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferenceStorage.isDoctor()){
                    drRequestAppointmentId(appointmentId);
                }
                else{
                    clientRequestAppointmentId(appointmentId);
                }
            }
        });
    }

    private void drRequestAppointmentId(long appointmentId) {
        Call<AgoraTokenGenerator> doctorCall = ServiceGenerator.getInstance().getApiConnector().getDoctorGeneratedToken(appointmentId);
        doctorCall.enqueue(new Callback<AgoraTokenGenerator>() {
            @Override
            public void onResponse(Call<AgoraTokenGenerator> call, Response<AgoraTokenGenerator> response) {
                if(response.body() != null && response.code() == 200){
                    AgoraTokenGenerator tokenGenerator = response.body();
                    String token = tokenGenerator.getToken();
                    String channelName = tokenGenerator.getChannelName();

                    Log.d("AGORA_TOKEN", token);
                    Log.d("AGORA_TOKEN_CHANNEL_NAME", channelName);


                    Intent intent = new Intent(AppointmentJoiningRoomActivity.this, VideoCallActivity.class);
                    intent.putExtra("TOKEN_ID", token);
                    intent.putExtra("CHANNEL_NAME", channelName);
                    intent.putExtra("APPOINTMENT_ID", appointmentId);
                    startActivity(intent);
                }

                else{
                    if(response.body().getError() != null){
                        Log.d("AGORA_TOKEN_ERROR", response.body().getError());
                    }
                    Toast.makeText(AppointmentJoiningRoomActivity.this, "Could not join meeting, something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgoraTokenGenerator> call, Throwable t) {
                Log.d("AGORA_SERVER_ERROR", String.valueOf(t.getStackTrace()));
            }
        });

    }
    private void clientRequestAppointmentId(long appointmentId){
        Call<AgoraTokenGenerator> clientCall = ServiceGenerator.getInstance().getApiConnector().getClientGeneratedToken(appointmentId);
        clientCall.enqueue(new Callback<AgoraTokenGenerator>() {
            @Override
            public void onResponse(Call<AgoraTokenGenerator> call, Response<AgoraTokenGenerator> response) {
                if(response.body() != null && response.code() == 200){
                    AgoraTokenGenerator tokenGenerator = response.body();
                    String token = tokenGenerator.getToken();
                    String channelName = tokenGenerator.getChannelName();

                    Log.d("AGORA_TOKEN", token);


                    Intent intent = new Intent(AppointmentJoiningRoomActivity.this, VideoCallActivity.class);
                    intent.putExtra("TOKEN_ID", token);
                    intent.putExtra("CHANNEL_NAME", channelName);
                    startActivity(intent);
                }

                else{
                    if(response.body().getError() != null){
                        Log.d("AGORA_TOKEN_ERROR", response.body().getError());
                    }
                    Toast.makeText(AppointmentJoiningRoomActivity.this, "Could not join meeting, something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgoraTokenGenerator> call, Throwable t) {
                Log.d("AGORA_SERVER_ERROR", String.valueOf(t.getStackTrace()));
            }
        });
    }

}