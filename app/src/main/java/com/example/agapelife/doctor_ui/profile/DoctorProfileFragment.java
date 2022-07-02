package com.example.agapelife.doctor_ui.profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorProfileFragment extends Fragment {
    TextInputEditText etFirstName, etLastName, etAbout, etExperience, etSpecialization, etEmail, etPhone, etHospital;
    ImageView profileImageView;

    String firstName, lastName, about, experience, specialization, phone, hospital, profileImage;
    Button btnUpdate;

    int GALLERY_REQUEST_CODE = 100;

    PreferenceStorage preferenceStorage;

    public DoctorProfileFragment() {
        // this should remain empty
    }

    public static DoctorProfileFragment newInstance() {
        DoctorProfileFragment fragment = new DoctorProfileFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

//        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        etFirstName = root.findViewById(R.id.et_firstname);
        etLastName = root.findViewById(R.id.et_lastname);
        etAbout = root.findViewById(R.id.et_about);
        etExperience = root.findViewById(R.id.et_experience);
        etSpecialization = root.findViewById(R.id.et_specialization);
        etPhone = root.findViewById(R.id.et_phone);
        etHospital = root.findViewById(R.id.et_hospital);
        profileImageView = root.findViewById(R.id.iv_doctor_dp);

        btnUpdate = root.findViewById(R.id.btn_update_details);

//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        preferenceStorage = new PreferenceStorage(requireActivity());
        getDoctorDetails();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInput(etFirstName, etLastName, etPhone);
            }
        });
    }

    private void imageChooser() {
        verifyPermissions();
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select an image"), GALLERY_REQUEST_CODE);
    }

    public boolean verifyPermissions(){
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
        };

        boolean isPermitted = false;

        if(
                ContextCompat.checkSelfPermission(
                        requireActivity().getApplicationContext(), permissions[0])
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        requireActivity().getApplicationContext(), permissions[1])
                        == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                        requireActivity().getApplicationContext(), permissions[2])
                        == PackageManager.PERMISSION_GRANTED
        ){
            isPermitted = true;
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(), permissions, 134);
        }

        return isPermitted;
    }

    private void validateInput(TextInputEditText etFirstName, TextInputEditText etLastName, TextInputEditText etPhone) {
        if(etFirstName.getText().toString().trim().isEmpty()){
            etFirstName.setError("Input first name");
        }
        else if(etLastName.getText().toString().trim().isEmpty()){
            etFirstName.setError("Input last name");
        }
        else if(etPhone.getText().toString().trim().isEmpty()){
            etPhone.setError("Input phone number");
        }
        else{
            updateDetails();
        }
    }

    private void updateDetails() {
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        phone = etPhone.getText().toString();
        specialization = etSpecialization.getText().toString();
        hospital = etHospital.getText().toString();
        experience = etExperience.getText().toString();
        about = etAbout.getText().toString();
    }

    private void getDoctorDetails() {
        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().getDoctorDetails(Long.parseLong(preferenceStorage.getUserId()));
        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                if(response.body() != null && response.code() == 200){
                    DoctorResponse doctorResponse = response.body();

                    etFirstName.setText(doctorResponse.getFirstName());
                    etLastName.setText(doctorResponse.getLastName());
                    etHospital.setText(doctorResponse.getHospital());
                    etExperience.setText(String.valueOf(doctorResponse.getExperienceYears()));
                    etPhone.setText(String.valueOf(doctorResponse.getPhoneNumber()));
                    etSpecialization.setText(doctorResponse.getSpeciality());
                    etAbout.setText(doctorResponse.getSelfDescription());

                    Glide.with(requireActivity())
                            .load(doctorResponse.getProfileImage())
                            .circleCrop()
                            .placeholder(R.drawable.doctor_img)
                            .into(profileImageView);

                }else{
                    Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {

            }
        });
    }

    //    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
}