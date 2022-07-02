package com.example.agapelife.networking.services;

import com.example.agapelife.models.Appointment;
import com.example.agapelife.models.AppointmentRequest;
import com.example.agapelife.networking.pojos.AgapeUserRequest;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.AgoraTokenGenerator;
import com.example.agapelife.networking.pojos.AppointmentResponse;
import com.example.agapelife.networking.pojos.DoctorRequest;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.pojos.MedicalReport;
import com.example.agapelife.networking.pojos.MedicalReportResponse;
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

    @GET("api/doctor/details/{id}")
    Call<DoctorResponse> getDoctorDetailsWithId(@Path("id") long id);

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

    @GET("api/clients/{pk}")
    Call<AgapeUserResponse> getPatientDetails(@Path("pk") long id);

    @GET("api/patients/{pk}")
    Call<AgapeUserResponse> getClientDetails(@Path("pk") long id);

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

    @GET("api/generate-client-meeting-tokens/{pk}")
    Call<AgoraTokenGenerator> getClientGeneratedToken(@Path("pk") long id);

    @GET("api/generate-doctor-meeting-tokens/{pk}")
    Call<AgoraTokenGenerator> getDoctorGeneratedToken(@Path("pk") long id);

    @GET("api/user-appointments/{pk}")
    Call<List<AppointmentResponse>> getUserAppointments(@Path("pk") long idNumber);

//    Get all patients with a certain doctor ID
    @GET("api/get-patients/{id}")
    Call<List<AgapeUserResponse>> getDoctorPatients(@Path("id") long id);

    @GET("api/appointments/{pk}")
    Call<AppointmentResponse> getAppointmentDetails(@Path("pk") long pk);

    @GET("api/get-medical-report/{id}")
    Call<List<MedicalReportResponse>> getDoctorsNotes(@Path("id") long idNumber);

    // Patient Request appointment
    @FormUrlEncoded
    @POST("api/book-appointment/")
    Call<AppointmentRequest> bookAppointment(
            @Field("client_id") long patientId,
            @Field("doctor_id") long doctorId,
            @Field("about") String about,
            @Field("patient_symptoms") String symptoms,
            @Field("perisistence_period") String persistencePeriod
    );

    // Doctor create appointment
    @FormUrlEncoded
    @POST("api/accept-appointment/")
    Call<Appointment> createAppointment(
            @Field("appointment_title") String appointmentTitle,
            @Field("start_time") String startTime,
            @Field("end_time") String endTime,
            @Field("doctor_id") long doctorId,
            @Field("patient_id") long patientId,
            @Field("appointment_id") long appointmentId
    );

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
    @POST("auth/doctor-signup/")
    Call<ResponseBody> registerDoctor(
            @PartMap Map<String, RequestBody> detailsMap,
            @Part MultipartBody.Part licenseFile,
            @Part MultipartBody.Part profileImg
            );


    @FormUrlEncoded
    @POST("auth/doctor/form/register/")
    Call<DoctorRequest> doctorForm(
            @Field("hospital") String hospital,
            @Field("category") String category,
            @Field("speciality") String speciality,
            @Field("username") String username,
            @Field("phone_number") String phoneNumber,
            @Field("id_number") String idNumber,
            @Field("password") String password,
            @Field("password2") String password2,
            @Field("email") String email,
            @Field("first_name") String firstName,
            @Field("last_name") String lastName,
            @Field("gender") int gender,
            @Field("age") int age,
            @Field("date_of_birth") String dateOfBirth
    );

    @FormUrlEncoded
    @POST("api/generate-medical-report/")
    Call<MedicalReport> createMedicalReport(
            @Field("appointment_id") long appointmentId,
            @Field("medication") String medication,
            @Field("doctor_report") String doctorReport
    );

    @Multipart
    @POST("auth/doctor/files/{id}")
    Call<DoctorRequest> uploadDocFiles(
            @Path("id") long id,
            @Part MultipartBody.Part licenseFile,
            @Part MultipartBody.Part profileImg
    );

}
