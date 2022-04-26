package com.example.agapelife.networking.services;

import com.example.agapelife.networking.pojos.AgapeUserRequest;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.AppointmentResponse;
import com.example.agapelife.networking.pojos.DoctorRequest;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.pojos.MedicalTipResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface EndPoints {
    // Doctors
    @GET("api/doctors/")
    Call<List<DoctorResponse>> getVerifiedDoctors();

    @GET("api/doctors/{id}")
    Call<DoctorResponse> getDoctorDetails(@Path("id") long id);

    @FormUrlEncoded
    @PUT("api/doctors/edit-status/{id}")
    Call<DoctorResponse> updateDoctorStatus(
            @Path("id") long id,
            @Field("is_available") boolean isAvailable
    );

    // Users
    @GET("api/users/")
    Call<List<AgapeUserResponse>> getUsers();

    @GET("api/users/{id}")
    Call<AgapeUserResponse> getUserDetails(@Path("id") long id);

    // Health tips
    @GET("api/health-tips/")
    Call<List<MedicalTipResponse>> getHealthTips();

    // Categories
    @GET("api/categories/")
    Call<List<MedicalCategoryResponse>> getMedicalCategories();

    // Appointments
    @GET("api/doctor-appointments/{pk}")
    Call<List<AppointmentResponse>> getDoctorAppointments(@Path("pk") long idNumber);

    @GET("api/client-appointments/{pk}")
    Call<List<AppointmentResponse>> getClientAppointments(@Path("pk") long idNumber);

    @GET("api/user-appointments/{pk}")
    Call<List<AppointmentResponse>> getUserAppointments(@Path("pk") long idNumber);

//    Get all patients with a certain doctor ID
    @GET("api/get-patients/{id}")
    Call<List<AgapeUserResponse>> getDoctorPatients(@Path("id") long id);


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

    @Multipart
    @POST("auth/doctor-register")
    Call<ResponseBody> registerDoctor(
            @PartMap Map<String, RequestBody> detailsMap,
            @Part MultipartBody.Part licenseFile,
            @Part MultipartBody.Part profileImg
            );

}
