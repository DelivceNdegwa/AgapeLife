package com.example.agapelife.user_ui.user_home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.R;
import com.example.agapelife.adapters.DoctorAdapter;
import com.example.agapelife.adapters.MedicalCategoryAdapter;
import com.example.agapelife.databinding.FragmentUserHomeBinding;
import com.example.agapelife.models.AgapeUser;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.pojos.MedicalTipResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeFragment extends Fragment {

    private UserHomeViewModel userHomeViewModel;
    private FragmentUserHomeBinding binding;
    private String id;

    DoctorAdapter doctorAdapter;
    MedicalCategoryAdapter categoryAdapter;

    RecyclerView rvDoctors;
    RecyclerView rvCategories;
    Chip chipAll;

    ConstraintLayout homeLayout, noConnectionLayout;

    PreferenceStorage preferenceStorage;

    TextView welcomeText;

    List<DoctorResponse> doctors = new ArrayList<>();
    List<DoctorResponse> filteredDoctors = new ArrayList<>();
    List<MedicalTipResponse> medicalTipResponses = new ArrayList<>();
    List<MedicalCategoryResponse> categories = new ArrayList<>();
    List<AgapeUser> users = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
        View root = inflater.inflate(R.layout.fragment_user_home, container, false);


        noConnectionLayout = root.findViewById(R.id.no_connection_layout);
        homeLayout = root.findViewById(R.id.home_layout);

        rvDoctors = root.findViewById(R.id.rv_doctor_list);
        rvDoctors.setNestedScrollingEnabled(true);
        rvDoctors.setLayoutManager(new LinearLayoutManager(root.getContext(),LinearLayoutManager.HORIZONTAL, false));

        rvCategories = root.findViewById(R.id.rv_categories);
        rvCategories.setNestedScrollingEnabled(true);
        rvCategories.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));


        preferenceStorage = new PreferenceStorage(requireActivity());
        welcomeText = binding.tvWelcome;

        chipAll = root.findViewById(R.id.chip_all);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        doctorAdapter = new DoctorAdapter(requireActivity(), doctors);
        categoryAdapter = new MedicalCategoryAdapter(categories, requireActivity(), this);

        getDoctors();
        getCategories();

        id = preferenceStorage.getUserId();
        welcomeText.setText("Hey "+preferenceStorage.getUserName()+"!");

        rvDoctors.setAdapter(doctorAdapter);
        rvCategories.setAdapter(categoryAdapter);

        chipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doctorAdapter = new DoctorAdapter(requireActivity(), doctors);
            }
        });

    }

    private void getCategories() {
        Call<List<MedicalCategoryResponse>> call = ServiceGenerator.getInstance().getApiConnector().getMedicalCategories();

        call.enqueue(new Callback<List<MedicalCategoryResponse>>() {
            @Override
            public void onResponse(Call<List<MedicalCategoryResponse>> call, Response<List<MedicalCategoryResponse>> response) {
                if(response.code() == 200 && response.body() != null){
                    categories.clear();
                    categories.addAll(response.body());
                    categoryAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MedicalCategoryResponse>> call, Throwable t) {

            }
        });
    }

    private void getDoctors() {

        Call<List<DoctorResponse>> call = ServiceGenerator.getInstance().getApiConnector().getVerifiedDoctors();
        call.enqueue(new Callback<List<DoctorResponse>>() {
            @Override
            public void onResponse(Call<List<DoctorResponse>> call, Response<List<DoctorResponse>> response) {
                if(response.code() == 200 && response.body() != null) {
                    doctors.clear();
                    doctors.addAll(response.body());
                    doctorAdapter.notifyDataSetChanged();
                    homeLayout.setVisibility(View.VISIBLE);
                    noConnectionLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<DoctorResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Check your connection and try again", Toast.LENGTH_SHORT).show();
                noConnectionLayout.setVisibility(View.VISIBLE);
                homeLayout.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void filterDoctorCategory(int categoryId) {
        doctorAdapter = new DoctorAdapter(getActivity(), filteredDoctors);

        for(DoctorResponse doctor : doctors){
            if(doctor.getCategory() == categoryId){
                filteredDoctors.add(doctor);

                Toast.makeText(getActivity(), String.valueOf(filteredDoctors), Toast.LENGTH_SHORT).show();
            }

            doctorAdapter.notifyDataSetChanged();
        }



    }
}