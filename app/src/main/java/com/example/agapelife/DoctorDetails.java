package com.example.agapelife;

import android.content.Intent;
import android.os.Bundle;

import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agapelife.databinding.ActivityDoctorDetailsBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDetails extends AppCompatActivity {

    private ActivityDoctorDetailsBinding binding;

    TextView tvDrName, tvDrSpecialization, tvDrExperienceYears, tvDrDescription;

    Button btnBookAppointment;
    FloatingActionButton fabMessage;

    Chip chipAbout, chipHospital;

    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDoctorDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(" ");

        Intent intent = getIntent();
        id = intent.getLongExtra("DOCTOR_ID", 0);
        toolbar.setTitle("Dr John Doe");

//        fabMessage = binding.fab;
//        fabMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        tvDrName = findViewById(R.id.tv_full_name);
        tvDrDescription = findViewById(R.id.tv_dr_description);
        tvDrExperienceYears = findViewById(R.id.tv_years_of_experience);
        tvDrSpecialization = findViewById(R.id.tv_specialization);

        btnBookAppointment = findViewById(R.id.btn_book_appointment);

        chipHospital = findViewById(R.id.chip_hospital);
        chipAbout = findViewById(R.id.chip_about);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDoctorDetails();
    }

    private void getDoctorDetails() {
        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().getDoctorDetails(id);

        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    DoctorResponse doctor = response.body();

                    updateViewValues(
                            "Dr "+doctor.getFirstName()+" "+doctor.getLastName(),
                            doctor.getSpeciality(),
                            doctor.getHospital()
                    );
                }
                else{
                    Toast.makeText(DoctorDetails.this, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DoctorDetails.this, UserMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                Toast.makeText(DoctorDetails.this, "Check your connection and try again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DoctorDetails.this, UserMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateViewValues(String name, String specialization, String hospital) {
        tvDrName.setText(name);
        tvDrSpecialization.setText(specialization);
        chipHospital.setText(hospital);
    }


}