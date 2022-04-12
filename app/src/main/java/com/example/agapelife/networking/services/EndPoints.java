package com.example.agapelife.networking.services;

import com.example.agapelife.networking.pojos.AgapeUserRequest;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.DoctorRequest;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.pojos.MedicalTipResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EndPoints {
    @GET("api/doctors/")
    Call<List<DoctorResponse>> getVerifiedDoctors();

    @GET("api/users/")
    Call<List<AgapeUserResponse>> getUsers();

    @GET("api/doctors/{id}")
    Call<DoctorResponse> getDoctorDetails(@Path("id") int id);

    @GET("api/health-tips/")
    Call<List<MedicalTipResponse>> getHealthTips();

    @GET("api/categories/")
    Call<List<MedicalCategoryResponse>> getMedicalCategories();

    @FormUrlEncoded
    @POST("auth/register")
    Call<AgapeUserRequest> signUpForm(
            @Field("username") String username,
            @Field("phone_number") String phoneNumber,
            @Field("id_number") String idNumber,
            @Field("password") String password,
            @Field("password2") String password2,
            @Field("email") String email,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName
    );

    @POST("auth/login")
    Call<AgapeUserResponse> loginForm(@Body AgapeUserResponse userResponse);

    @FormUrlEncoded
    @POST("auth/doctor-register")
    Call<DoctorRequest> doctorRegister(
            @Field("license_certificate") String license,
            @Field("profile_image") String profileImage,
            @Field("hospital") String hospital,
            @Field("speciality") String speciality,
            @Field("category") String category,
            @Field("username") String username,
            @Field("phone_number") String phoneNumber,
            @Field("id_number") String idNumber,
            @Field("password") String password,
            @Field("password2") String password2,
            @Field("email") String email,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName
    );

//    @GET("feedbacks/")
//String username, String phoneNumber, String idNumber, String password, String password2, String email, String firstName, String lastName


}
