package com.example.agapelife.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.R;
import com.example.agapelife.adapters.DoctorAdapter;
import com.example.agapelife.databinding.FragmentHomeBinding;
import com.example.agapelife.models.AgapeUser;
import com.example.agapelife.models.Doctor;
import com.example.agapelife.networking.ApiCalls;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalTipResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String id;

    List<MedicalTipResponse> medicalTipResponses = new ArrayList<>();
    DoctorAdapter doctorAdapter;
    RecyclerView rvDoctors;

    PreferenceStorage preferenceStorage;

    TextView welcomeText;

    List<Doctor> doctors = new ArrayList<>();
    List<AgapeUser> users = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        welcomeText = binding.textHome;

        rvDoctors = binding.rvDoctors;

        rvDoctors.setNestedScrollingEnabled(true);
        rvDoctors.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
        doctorAdapter = new DoctorAdapter(requireActivity(), doctors);
        rvDoctors.setAdapter(doctorAdapter);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        preferenceStorage = new PreferenceStorage(requireActivity());



    }

    @Override
    public void onResume() {
        super.onResume();

        id = preferenceStorage.getUserId();
        Toast.makeText(getActivity(), "Your ID is: "+id, Toast.LENGTH_SHORT).show();



        welcomeText.setText(String.format("Your ID is:%s", id));
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        getDoctors();
    }

//    private void allUsers() {
//        Call<List<AgapeUserResponse>> call = ServiceGenerator.getInstance().getApiConnector().getUsers();
//
//        call.enqueue(new Callback<List<AgapeUserResponse>>() {
//            @Override
//            public void onResponse(Call<List<AgapeUserResponse>> call, Response<List<AgapeUserResponse>> response) {
//                if(response.code() == 200){
//                    users.clear();
//                    for(int i = 0; i < response.body().size(); i++){
//                       AgapeUserResponse userreponse = response.body().get(i);
//                       AgapeUser user = new AgapeUser();
//                       user.setFirstName(userreponse.getFirstName());
//                       user.setLastName(userreponse.getLastName());
//                       user.setId(userreponse.getId());
//                       users.add(user);
//                    }
//                    doctorAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<AgapeUserResponse>> call, Throwable t) {
//
//            }
//        });
//    }

    private void getDoctors() {
        Call<List<DoctorResponse>> call = ServiceGenerator.getInstance().getApiConnector().getVerifiedDoctors();
        call.enqueue(new Callback<List<DoctorResponse>>() {
            @Override
            public void onResponse(Call<List<DoctorResponse>> call, Response<List<DoctorResponse>> response) {
                Toast.makeText(getActivity(), "onResponse"+response.code(), Toast.LENGTH_SHORT).show();

                if(response.code() == 200) {
                    Toast.makeText(getActivity(), "onResponseSuccessful", Toast.LENGTH_SHORT).show();
                    String firstname = "", lastname = "";

                    for (int i = 0; i < response.body().size(); i++) {
                        DoctorResponse doctorResponse = response.body().get(i);
                        Toast.makeText(getActivity(), doctorResponse.getFirstName(), Toast.LENGTH_SHORT).show();
                        doctors.add(new Doctor(doctorResponse.getId(), doctorResponse.getProfileImage(), doctorResponse.getHospital(), doctorResponse.getSpeciality(), firstname, lastname));

                    }
                    Log.d("DOCTORS_TEST", String.valueOf(doctors));
                }
            }

            @Override
            public void onFailure(Call<List<DoctorResponse>> call, Throwable t) {
                Log.d("DOCTOR_onFailure", String.valueOf(call)+t.toString());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}