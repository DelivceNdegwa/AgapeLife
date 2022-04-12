package com.example.agapelife.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.agapelife.models.Doctor;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.pojos.MedicalTipResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCalls {
    // Get a list of doctors from the server side
    Context context;

    public ApiCalls(Context context) {
        this.context = context;
    }

    public List<DoctorResponse> getVerifiedDoctors(List<DoctorResponse> doctorResponses){
        Call<List<DoctorResponse>> call = ServiceGenerator.getInstance().getApiConnector().getVerifiedDoctors();

        call.enqueue(new Callback<List<DoctorResponse>>() {
            @Override
            public void onResponse(Call<List<DoctorResponse>> call, Response<List<DoctorResponse>> response) {
                if(response.code() == 200) {
                    doctorResponses.clear();
                    for (int i = 0; i < response.body().size(); i++) {
                        DoctorResponse doctorResponse = response.body().get(i);
                        Log.d("::DOCTOR" + i + ":::", doctorResponse.getSpeciality()+" "+doctorResponse.getIsVerified());
                        doctorResponses.add(doctorResponse);
                    }

                }

            }

            @Override
            public void onFailure(Call<List<DoctorResponse>> call, Throwable t) {
                displayErrorMessage();
            }
        });
        return doctorResponses;
    }

    // Get health tips from the server side
    public List<MedicalTipResponse> getHealthTips(List<MedicalTipResponse> medicalTipResponses){
        medicalTipResponses = new ArrayList<>();

        Call<List<MedicalTipResponse>> call = ServiceGenerator.getInstance().getApiConnector().getHealthTips();

        List<MedicalTipResponse> finalMedicalTipResponses = medicalTipResponses;
        call.enqueue(new Callback<List<MedicalTipResponse>>() {
            @Override
            public void onResponse(Call<List<MedicalTipResponse>> call, Response<List<MedicalTipResponse>> response) {
                if(response.code() == 200 && !response.body().isEmpty()){

                    for(int i = 0; i < response.body().size(); i++){
                       MedicalTipResponse tipResponse = response.body().get(i);
                       finalMedicalTipResponses.add(tipResponse);
                    }
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MedicalTipResponse>> call, Throwable t) {
                displayErrorMessage();
            }

        });
        return finalMedicalTipResponses;
    }

    public List<MedicalCategoryResponse> getMedicalCategories(List<MedicalCategoryResponse> categories){
        Call<List<MedicalCategoryResponse>> call = ServiceGenerator.getInstance().getApiConnector().getMedicalCategories();
        call.enqueue(new Callback<List<MedicalCategoryResponse>>() {
            @Override
            public void onResponse(Call<List<MedicalCategoryResponse>> call, Response<List<MedicalCategoryResponse>> response) {
                if(response.code() == 200 && !response.body().isEmpty()){
                    for(int i=0; i <response.body().size(); i++){
                        MedicalCategoryResponse category = response.body().get(i);
                        categories.add(category);
                    }
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MedicalCategoryResponse>> call, Throwable t) {
                displayErrorMessage();
            }
        });

        return categories;
    }

//    public DoctorResponse getDoctorDetails(int id){
//        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().getDoctorDetails(id);
//
//        call.enqueue(new Callback<DoctorResponse>() {
//            @Override
//            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
//                if(response.code() == 200){
//                    DoctorResponse doctorResponse = response.body();
//
//                }
//                else{
//                    Toast.makeText(context, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DoctorResponse> call, Throwable t) {
//                displayErrorMessage();
//            }
//        });
//        return doctorResponses;
//    }



    private void displayErrorMessage(){

//        Snackbar.make(view, "Check your internet connection", Snackbar.LENGTH_LONG);
        Toast.makeText(context, "Check your internet connection", Toast.LENGTH_LONG).show();
    }
}
