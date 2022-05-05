package com.example.agapelife.doctor_ui.home;

import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.R;
import com.example.agapelife.adapters.ConsultationsAdapter;
import com.example.agapelife.adapters.PatientsAdapter;
//import com.example.agapelife.databinding.FragmentHomeBinding;
import com.example.agapelife.doctor_ui.dashboard.DashboardFragment;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.AppointmentResponse;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;

import java.lang.ref.PhantomReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

//    private FragmentHomeBinding binding;
    PreferenceStorage preferenceStorage;

    ConsultationsAdapter consultationsAdapter;
    PatientsAdapter patientsAdapter;

    RecyclerView rvConsultations, rvRecentPatients;
    ConstraintLayout notVerifiedLayout, verifiedLayout, recentPatientsLayout, consultationLayout;

    View noConnectionLayout, noPatientsLayout;

    Switch onlineStatus;
    TextView doctorGreeting, cardTvFname, tvFirstName, tvOnlineStatus, tvNoConnectionTitle;


    List<MedicalCategoryResponse> medicalCategoryResponses = new ArrayList<>();
    List<AppointmentResponse> consultations = new ArrayList<>();
    List<AgapeUserResponse> patients = new ArrayList<>();
    DoctorResponse doctorDetails = new DoctorResponse();

    boolean noPatients=false, noConsultations=false;

    boolean isOnline = false;
    String status;

    public HomeFragment() {
        // this should remain empty
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

//        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        verifiedLayout = root.findViewById(R.id.verified_layout);
        notVerifiedLayout = root.findViewById(R.id.not_verified_layout);
        recentPatientsLayout = root.findViewById(R.id.recent_patients_layout);
        consultationLayout = root.findViewById(R.id.consultation_layout);

        noConnectionLayout = root.findViewById(R.id.inc_no_connection);
        noPatientsLayout = root.findViewById(R.id.view_no_patient);

        doctorGreeting = root.findViewById(R.id.doctor_greeting);
        cardTvFname = root.findViewById(R.id.txt_first_letter_name);
        tvFirstName = root.findViewById(R.id.f_name);
        tvNoConnectionTitle = noConnectionLayout.findViewById(R.id.tv_no_connection_heading);

        onlineStatus = root.findViewById(R.id.switch_status);
        tvOnlineStatus = root.findViewById(R.id.tv_online_status);

        preferenceStorage = new PreferenceStorage(getActivity());

        rvConsultations = root.findViewById(R.id.rv_appointments);
        rvConsultations.setNestedScrollingEnabled(true);
        rvConsultations.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvRecentPatients = root.findViewById(R.id.rv_recent_patients);
        rvRecentPatients.setNestedScrollingEnabled(true);
        rvRecentPatients.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));

        return root;
    }



//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }

    @Override
    public void onResume() {
        super.onResume();
        consultationsAdapter = new ConsultationsAdapter(getActivity(), consultations);
        patientsAdapter = new PatientsAdapter(getActivity(), patients);

        String firstName = preferenceStorage.getFirstName();
        String lastName = preferenceStorage.getLastName();

        String firstNameInitial = Character.toString(firstName.charAt(0));
        String lastNameInitial = Character.toString(lastName.charAt(0));

        doctorGreeting.setText("Hi "+preferenceStorage.getFirstName());

        tvFirstName.setText(firstName);
        tvNoConnectionTitle.setText("Hey "+firstName);

        if(!firstName.isEmpty()){
            String nameInitials = firstNameInitial + lastNameInitial;
            cardTvFname.setText(nameInitials);
        }
        else{
            cardTvFname.setVisibility(View.GONE);
        }

        getConsultations();
        rvConsultations.setAdapter(consultationsAdapter);

        loadPatients();

        if(checkIfEmpty()){
            recentPatientsLayout.setVisibility(View.GONE);
            consultationLayout.setVisibility(View.GONE);
            noPatientsLayout.setVisibility(View.VISIBLE);
        }
        else{
            recentPatientsLayout.setVisibility(View.VISIBLE);
            consultationLayout.setVisibility(View.VISIBLE);
            noPatientsLayout.setVisibility(View.GONE);
        }
        getDoctorDetails();



        onlineStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    tvOnlineStatus.setText(String.valueOf(onlineStatus.getTextOn()));
                    isOnline = true;
                }
                else{
                    tvOnlineStatus.setText(String.valueOf(onlineStatus.getTextOff()));
                    isOnline = false;
                }
                preferenceStorage.setIsAvailableStatus(isOnline);
                changeOnlineStatus();
                Toast.makeText(getActivity(), "You are "+preferenceStorage.isAvailable(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean checkIfEmpty() {
        if(noConsultations && noPatients){
            return true;
        }
        return false;
    }

    private void changeOnlineStatus() {
        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().updateDoctorStatus(
                Long.parseLong(preferenceStorage.getUserId()),
                isOnline
        );
        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                noConnectionLayout.setVisibility(View.GONE);
                if(response.code() == 200){
                    noConnectionLayout.setVisibility(View.GONE);
                    verifiedLayout.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), "Check your connection", Toast.LENGTH_SHORT).show();
                verifiedLayout.setVisibility(View.GONE);
                noConnectionLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadPatients() {
        Call<List<AgapeUserResponse>> call = ServiceGenerator.getInstance().getApiConnector().getDoctorPatients(Long.parseLong(preferenceStorage.getUserId()));

        call.enqueue(new Callback<List<AgapeUserResponse>>() {
            @Override
            public void onResponse(Call<List<AgapeUserResponse>> call, Response<List<AgapeUserResponse>> response) {
                noConnectionLayout.setVisibility(View.GONE);
                if((response.code() == 500)){
                    verifiedLayout.setVisibility(View.GONE);
                    noConnectionLayout.setVisibility(View.VISIBLE);
                }
                else if(response.code() == 200 && response.body() != null){
                    patients.addAll(response.body());
                    patientsAdapter.notifyDataSetChanged();
                    noConnectionLayout.setVisibility(View.GONE);
                    verifiedLayout.setVisibility(View.VISIBLE);
                    noPatients = false;
                }
                else if(response.body() == null && response.code() == 200){
                    noPatients = true;
//                    Toast.makeText(getContext(), "You have no patients currently", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("onResponseErrorCode", String.valueOf(response.code()));
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFailure(Call<List<AgapeUserResponse>> call, Throwable t) {
                verifiedLayout.setVisibility(View.GONE);
                noConnectionLayout.setVisibility(View.VISIBLE);
                Log.d("onFailureMessage", t.getMessage());
                Log.d("onFailureStackTrace", String.valueOf(Arrays.stream(t.getStackTrace()).toArray()));
            }
        });
    }

    private void getConsultations() {
        Call<List<AppointmentResponse>> call = ServiceGenerator.getInstance().getApiConnector().getDoctorAppointments(Long.parseLong(preferenceStorage.getUserId()));
        call.enqueue(new Callback<List<AppointmentResponse>>() {
            @Override
            public void onResponse(Call<List<AppointmentResponse>> call, Response<List<AppointmentResponse>> response) {
                noConnectionLayout.setVisibility(View.GONE);
                if(response.body() != null && response.code() == 200){
                    consultations.addAll(response.body());
                    consultationsAdapter.notifyDataSetChanged();
                    noConsultations = false;
                }
                else if(response.body() == null && response.code() == 200){
                    noConsultations = true;
                }
                else{
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("onResponseErrorCode", String.valueOf(response.code()));
                    Log.d("onResponseErrorBody", String.valueOf(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<AppointmentResponse>> call, Throwable t) {
                verifiedLayout.setVisibility(View.GONE);
                noConnectionLayout.setVisibility(View.VISIBLE);
                Log.d("onFailureThrowable", t.getMessage());
                Log.d("onFailureStackTrace", Arrays.toString(t.getStackTrace()));
            }
        });
    }

    private void getDoctorDetails(){
        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().getDoctorDetails(Long.parseLong(preferenceStorage.getUserId()));
        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                noConnectionLayout.setVisibility(View.GONE);
                if(response.code() == 200 && response.body() !=null){
                    doctorDetails = response.body();
                    if(!doctorDetails.getIsVerified()){
                         notVerifiedLayout.setVisibility(View.VISIBLE);
                         verifiedLayout.setVisibility(View.GONE);
                    }

                }
                else{
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("onResponseErrorMessage", String.valueOf(response.body()));
                    Log.d("onResponseCode", String.valueOf(response.code()));
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
//                Toast.makeText(getActivity(), "Check your connection", Toast.LENGTH_SHORT).show();
                Log.d("onFailureMessage", t.getMessage());
                Log.d("onFailureStackTrace", String.valueOf(Arrays.stream(t.getStackTrace()).toArray()));
            }
        });
    }

    public void filterDoctorCategory(String categoryName){
        Toast.makeText(getActivity(), categoryName, Toast.LENGTH_SHORT);
    }

}