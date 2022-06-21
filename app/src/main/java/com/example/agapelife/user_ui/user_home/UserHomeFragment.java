package com.example.agapelife.user_ui.user_home;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.agapelife.adapters.AppointmentRequestsAdapter;
import com.example.agapelife.adapters.AppointmentsAdapter;
import com.example.agapelife.adapters.DoctorAdapter;
import com.example.agapelife.adapters.MedicalCategoryAdapter;
import com.example.agapelife.adapters.OnlineDoctorAdapter;
import com.example.agapelife.databinding.FragmentUserHomeBinding;
import com.example.agapelife.models.AgapeUser;
import com.example.agapelife.models.Appointment;
import com.example.agapelife.models.AppointmentRequest;
import com.example.agapelife.models.OnlineDoctor;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.pojos.MedicalTipResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.networking.services.URLs;
import com.example.agapelife.utils.PreferenceStorage;
import com.google.android.material.chip.Chip;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeFragment extends Fragment {

    private UserHomeViewModel userHomeViewModel;
    private String id, message;

    private WebSocketClient mWebSocketClient;
    private final String SOCKET_URL = URLs.ONLINE_DOCTORS;
    List<OnlineDoctor> onlineDoctorList = new ArrayList<>();
    private FragmentUserHomeBinding binding;
    DoctorAdapter doctorAdapter;
    MedicalCategoryAdapter categoryAdapter;
    OnlineDoctorAdapter onlineDoctorAdapter;
    AppointmentRequestsAdapter appointmentRequestsAdapter;
    AppointmentsAdapter appointmentsAdapter;

    RecyclerView rvDoctors;
    RecyclerView rvCategories;
    RecyclerView recyclerViewUpcoming;
    Chip chipAll;


    ConstraintLayout homeLayout, noConnectionLayout;

    PreferenceStorage preferenceStorage;

    TextView welcomeText, tvUpcomingAppointments;

    List<DoctorResponse> doctors = new ArrayList<>();
    List<DoctorResponse> filteredDoctors = new ArrayList<>();
    List<MedicalTipResponse> medicalTipResponses = new ArrayList<>();
    List<MedicalCategoryResponse> categories = new ArrayList<>();
    List<AgapeUser> users = new ArrayList<>();

    List<AppointmentRequest> appointmentRequests = new ArrayList<>();
    List<Appointment> upcomingAppointments = new ArrayList<>();

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

        tvUpcomingAppointments = root.findViewById(R.id.tv_upcoming_appointments);

//        rvUpcomingAppointments.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpcoming = root.findViewById(R.id.recycler_view_upcoming);
        recyclerViewUpcoming.setNestedScrollingEnabled(true);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));

        preferenceStorage = new PreferenceStorage(requireActivity());
        welcomeText = root.findViewById(R.id.tv_welcome);

        chipAll = root.findViewById(R.id.chip_all);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        doctorAdapter = new DoctorAdapter(requireActivity(), doctors);
        onlineDoctorAdapter = new OnlineDoctorAdapter(requireActivity(), onlineDoctorList);
        categoryAdapter = new MedicalCategoryAdapter(categories, requireActivity(), this);
        appointmentRequestsAdapter = new AppointmentRequestsAdapter(getActivity(), appointmentRequests);
        appointmentsAdapter = new AppointmentsAdapter(getActivity(), upcomingAppointments, preferenceStorage);

        getOnlineDoctors();
//        getDoctors();
        fetchAppointmentRequests();
        getCategories();

        id = preferenceStorage.getUserId();
        welcomeText.setText("Hey "+getActivity().getIntent().getStringExtra("FIRST_NAME"));

        rvDoctors.setAdapter(onlineDoctorAdapter);
        rvCategories.setAdapter(categoryAdapter);
        recyclerViewUpcoming.setAdapter(appointmentsAdapter);

        if(upcomingAppointments.isEmpty()){
            recyclerViewUpcoming.setVisibility(View.GONE);
            tvUpcomingAppointments.setVisibility(View.GONE);
        }


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

    // using retrofit
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

    public URI loadUri(String socketURI){
        URI uri;
        try{
            uri = new URI(URLs.BASE_PATIENT_URL+preferenceStorage.getUserId()+"/appointments/");
            return uri;
        }
        catch(URISyntaxException e){
            e.printStackTrace();
            return null;
        }
    }


    // using sockets
    private void getOnlineDoctors() {
        URI uri;
        try {
            uri = new URI(SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Websocket", "Opened");
                JSONObject socketMessage = new JSONObject();
                try {
                    socketMessage.put("message", "Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                    mWebSocketClient.send(String.valueOf(socketMessage));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMessage(String s) {
                message = s;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("Websocket_msg", message);
                        fetchDoctorData(message);

                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    public void fetchAppointmentRequests(){
        URI uri = loadUri(URLs.BASE_PATIENT_URL+preferenceStorage.getUserId()+"/appointments/");

        if(uri != null){
            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.i("PATIENT_APPOINTMENT_SOCKET", "Opened");
                    JSONObject socketMessage = new JSONObject();
                    try {
                        socketMessage.put("handshake", "Handshake successful");
                        mWebSocketClient.send(String.valueOf(socketMessage));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onMessage(String message) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("PATIENT_APPOINTMENTS_MESSAGE", String.valueOf(message.length()));

                            parseJsonData(message);
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.i("Websocket", "Closed " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.i("Websocket", "Error " + ex.getMessage());
                }
            };
            mWebSocketClient.connect();
        }
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

    private void fetchDoctorData(String message) {
        try {
            JSONObject listJson = new JSONObject(message);
            JSONArray doctorsArray = listJson.getJSONArray("doctor_list");

            onlineDoctorList.clear();
            for(int i=0; i < doctorsArray.length(); i++){
                JSONObject doctorObject = doctorsArray.getJSONObject(i);
                addDoctor(doctorObject);
            }

            Log.i("Websocket_list", String.valueOf(onlineDoctorList));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addDoctor(JSONObject doctorObject) {
        OnlineDoctor doctor = null;
        try {
            doctor = new OnlineDoctor(
                    doctorObject.getLong("id"),
                    doctorObject.getLong("id_number"),
                    doctorObject.getLong("phone_number"),
                    doctorObject.getBoolean("is_verified"),
                    doctorObject.getBoolean("is_available"),
                    doctorObject.getString("profile_image"),
                    doctorObject.getString("first_name"),
                    doctorObject.getString("last_name"),
                    doctorObject.getString("hospital"),
                    doctorObject.getString("specialization")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        onlineDoctorList.add(doctor);
        onlineDoctorAdapter.notifyDataSetChanged();
        homeLayout.setVisibility(View.VISIBLE);
        noConnectionLayout.setVisibility(View.GONE);
        Log.i("Websocket_list_obj", String.valueOf(doctor.getProfileImage()));
    }

    private void parseJsonData(String message) {
        try {
            JSONObject appointmentList = new JSONObject(message);
            Log.d("appointmentsList", String.valueOf(appointmentList));
            JSONArray appointmentsArray = appointmentList.getJSONArray("appointment_requests_list");
            Log.d("appointmentsArray", String.valueOf(appointmentsArray));
            JSONArray upcomingAppointmentsArray = appointmentList.getJSONArray("upcoming_appointments_list");
            Log.d("appointmentsArray_upcoming", String.valueOf(upcomingAppointmentsArray));

            loadAppointmentRequests(appointmentsArray);
            loadUpcomingAppointments(upcomingAppointmentsArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadUpcomingAppointments(JSONArray upcomingAppointmentsArray) throws JSONException {
        upcomingAppointments.clear();

        for(int i=0; i < upcomingAppointmentsArray.length(); i++){
            JSONObject appointment = upcomingAppointmentsArray.getJSONObject(i);
            addUpcomingAppointment(appointment);
        }

    }

    private void addUpcomingAppointment(JSONObject appointment) {
        Appointment upcomingAppointment = null;
        try {

            upcomingAppointment = new Appointment(
                    appointment.getString("title"),
                    appointment.getString("start_time"),
                    appointment.getString("end_time"),
                    appointment.getLong("doctor"),
                    appointment.getLong("client"),
                    appointment.getLong("id"),
                    appointment.getInt("status"),
                    appointment.getString("doctor_first_name"),
                    appointment.getString("doctor_last_name"),
                    appointment.getString("doctor_profile_image"),
                    false
            );
            upcomingAppointments.add(upcomingAppointment);

            for(Appointment aptRst : upcomingAppointments){
                Log.d("ITEM_LIST", aptRst.getAppointmentTitle());
            }

            appointmentsAdapter.notifyDataSetChanged();

            recyclerViewUpcoming.setVisibility(View.VISIBLE);
            tvUpcomingAppointments.setVisibility(View.VISIBLE);




        } catch (JSONException e) {
            Log.d("BIG_ERROR_MESSAGE", e.getMessage());
            Log.d("BIG_ERROR_TRACE", String.valueOf(e.getStackTrace()));
            Log.d("BIG_ERROR_STRING", e.toString());
        }





    }

    private void loadAppointmentRequests(JSONArray appointmentsArray) throws JSONException {
        appointmentRequests.clear();

        for(int i=0; i < appointmentsArray.length(); i++){
            JSONObject appointmentObj = appointmentsArray.getJSONObject(i);
            addAppointmentRequest(appointmentObj);
        }

//        consultationLayout.setVisibility(View.VISIBLE);
//        noPatientsLayout.setVisibility(View.GONE);
//        tvConsultations.setVisibility(View.VISIBLE);
    }

    private void addAppointmentRequest(JSONObject appointment) {
        AppointmentRequest appointmentRequest = null;
        try {
            appointmentRequest = new AppointmentRequest(
                    appointment.getLong("id"),
                    appointment.getLong("client"),
                    appointment.getLong("doctor"),
                    appointment.getInt("status"),
                    appointment.getString("about"),
                    appointment.getBoolean("read"),
                    appointment.getString("persistence_period"),
                    appointment.getString("symptoms")
            );

            appointmentRequests.add(appointmentRequest);
            recyclerViewUpcoming.setVisibility(View.VISIBLE);
            tvUpcomingAppointments.setVisibility(View.VISIBLE);

            for(AppointmentRequest aptRst : appointmentRequests){
                Log.d("ITEM_LIST", aptRst.getAbout());
            }

            appointmentRequestsAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}