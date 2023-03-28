package com.example.agapelife.instant_appointments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.InstantPatientRequest;
import com.example.agapelife.networking.pojos.PatientRegistrationResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPatientActivity extends AppCompatActivity {

    TextInputEditText registerFirstName, registerLastName, registerNationalID, registerPhone, registerAge;
    Button btnRegisterPatient;

    PreferenceStorage preferenceStorage;

    String firstName;
    String lastName;
    String nationalID;
    String phone;
    String age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

        registerFirstName = findViewById(R.id.input_register_first_name);
        registerLastName = findViewById(R.id.input_register_last_name);
        registerNationalID = findViewById(R.id.input_register_national_id);
        registerPhone = findViewById(R.id.input_register_phone);
        registerAge = findViewById(R.id.input_register_age);
        btnRegisterPatient = findViewById(R.id.btn_register_patient);

        preferenceStorage = new PreferenceStorage(this);


        btnRegisterPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = registerFirstName.getText().toString().trim();
                lastName = registerLastName.getText().toString().trim();
                nationalID = registerNationalID.getText().toString();
                phone = registerPhone.getText().toString();
                age = registerAge.getText().toString();
                validateRegisterInputs();
            }
        });
    }

    private boolean validateRegisterInputs() {
        boolean isValid = false;
        if(firstName.trim().isEmpty()){
            registerFirstName.setError("Input first name");
        }
        else if(lastName.trim().isEmpty()){
            registerFirstName.setError("Input last name");
        }
        else if(nationalID.trim().isEmpty()){
            registerNationalID.setError("Input national id");
        }
        else if(phone.trim().isEmpty()){
            registerPhone.setError("Input phone number");
        }
        else if(age.trim().isEmpty()){
            registerAge.setError("Input age");
        }
        else{
            isValid = true;
            registerPatientDetails();
        }
        return isValid;
    }
    //String firstName, String lastName, long nationalID, long age, long phone, long doctorID
    private void registerPatientDetails() {
        Call<PatientRegistrationResponse> call = ServiceGenerator.getInstance().getApiConnector().patientForm(
                firstName,
                lastName,
                Long.parseLong(nationalID),
                Long.parseLong(age),
                Long.parseLong(phone),
                Long.parseLong(preferenceStorage.getUserId())
        );

        call.enqueue(new Callback<PatientRegistrationResponse>() {
            @Override
            public void onResponse(Call<PatientRegistrationResponse> call, Response<PatientRegistrationResponse> response) {
                if(response.code() == 200 || response.code() == 201){
                    Log.d("REGISTER_PATIENT", String.valueOf(response.body().getMessage()));
//                    Toast.makeText(RegisterPatientActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterPatientActivity.this, InstantAppointmentActivity.class);
                    intent.putExtra("MESSAGE", response.body().getMessage());
                    intent.putExtra("PATIENT_CREATED", response.body().getCreated());
                    intent.putExtra("PATIENT_DB_ID", response.body().getPatientId());
                    intent.putExtra("FULL_NAME", firstName+" "+lastName);
                    intent.putExtra("NATIONAL_ID", nationalID);
                    intent.putExtra("PHONE", phone);
                    intent.putExtra("AGE", age);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(
                            RegisterPatientActivity.this,
                            "Something went wrong, try again",
                            Toast.LENGTH_LONG
                    );
                }
            }

            @Override
            public void onFailure(Call<PatientRegistrationResponse> call, Throwable t) {
                Toast.makeText(RegisterPatientActivity.this, "We are encontering a techical issue, please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }
}