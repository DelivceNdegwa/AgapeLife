package com.example.agapelife.doctor_ui.home;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.R;
import com.example.agapelife.adapters.AppointmentRequestsAdapter;
import com.example.agapelife.adapters.AppointmentsAdapter;
import com.example.agapelife.adapters.ConsultationsAdapter;
import com.example.agapelife.adapters.PatientsAdapter;
//import com.example.agapelife.databinding.FragmentHomeBinding;
import com.example.agapelife.doctors.DoctorsSection;
import com.example.agapelife.instant_appointments.RegisterPatientActivity;
import com.example.agapelife.models.Appointment;
import com.example.agapelife.models.AppointmentRequest;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.AppointmentResponse;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.pojos.MedicalCategoryResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.networking.services.URLs;
import com.example.agapelife.utils.PreferenceStorage;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
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
    AppointmentRequestsAdapter appointmentRequestsAdapter;
    AppointmentsAdapter appointmentsAdapter;

    RecyclerView rvConsultations, rvAppointmentRequests;
    ConstraintLayout notVerifiedLayout, verifiedLayout, recentPatientsLayout, consultationLayout;

    View noConnectionLayout, noPatientsLayout;

    TextView tvConsultations, tvAppointmentsNumber, tvRequestsNumber;
    Switch onlineStatus;
    TextView doctorGreeting, cardTvFname, tvFirstName, tvOnlineStatus, tvNoConnectionTitle;
    Button btnRegisterInstantPatient;

    private WebSocketClient mWebSocketClient;

    List<MedicalCategoryResponse> medicalCategoryResponses = new ArrayList<>();
    List<AppointmentResponse> consultations = new ArrayList<>();
    List<AppointmentRequest> appointmentRequests = new ArrayList<>();
    List<Appointment> upcomingAppointments = new ArrayList<>();
    List<AgapeUserResponse> patients = new ArrayList<>();
    DoctorResponse doctorDetails = new DoctorResponse();

    boolean noPatients=false,
            noConsultations=false;

    boolean isOnline = false;
    String status, notificationMessage;

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

        tvConsultations = root.findViewById(R.id.tv_consultations);

        preferenceStorage = new PreferenceStorage(getActivity());

        tvAppointmentsNumber = root.findViewById(R.id.tv_upcoming_value);
        tvRequestsNumber = root.findViewById(R.id.tv_request_value);

        //       Appointment will be termed to as Bookings
        rvConsultations = root.findViewById(R.id.rv_appointments);
        rvConsultations.setNestedScrollingEnabled(true);
        rvConsultations.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));

        //      AppointmentRequests will be used
        rvAppointmentRequests = root.findViewById(R.id.rv_appointment_requests);
        btnRegisterInstantPatient = root.findViewById(R.id.btn_register_instant_patient);
        rvAppointmentRequests.setNestedScrollingEnabled(true);
        rvAppointmentRequests.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));

        onlineStatus.setChecked(preferenceStorage.isAvailableBool());


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

        btnRegisterInstantPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), RegisterPatientActivity.class);
                startActivity(i);
//                Intent intent = new Intent(getActivity(), RegisterPatientActivity.class);
//                startActivity(intent);
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();

        addNotification("You have an appointment in 10 minutes", 3);
        consultationsAdapter = new ConsultationsAdapter(getActivity(), consultations);
        patientsAdapter = new PatientsAdapter(getActivity(), patients);
        appointmentRequestsAdapter = new AppointmentRequestsAdapter(getActivity(), appointmentRequests);
        appointmentsAdapter = new AppointmentsAdapter(getActivity(), upcomingAppointments, preferenceStorage);

        notificationMessage = requireActivity().getIntent().getStringExtra("notification_message");

        String firstName = preferenceStorage.getFirstName();
        String lastName = preferenceStorage.getLastName();

        String firstNameInitial = Character.toString(firstName.charAt(0));
        String lastNameInitial = Character.toString(lastName.charAt(0));
//        preferenceStorage.getFirstName()
        doctorGreeting.setText("Hi "+preferenceStorage.getFirstName());

        tvFirstName.setText("Doctor "+preferenceStorage.getFirstName());
        tvNoConnectionTitle.setText("Hi "+preferenceStorage.getLastName());

        if(!firstName.isEmpty()){
            String nameInitials = firstNameInitial + lastNameInitial;
            cardTvFname.setText(nameInitials);
        }
        else{
            cardTvFname.setVisibility(View.GONE);
        }

//        getConsultations();
//        fetchAppointmentRequests();
//        loadPatients();
        getDoctorDetails();

//        rvConsultations.setAdapter(appointmentRequestsAdapter);
        rvConsultations.setAdapter(appointmentsAdapter);
//        rvAppointmentRequests.setAdapter(appointmentRequestsAdapter);



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
//                    noPatients = false;
                }
                else if(response.body() == null && response.code() == 200){
//                    noPatients = true;

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
//                verifiedLayout.setVisibility(View.GONE);
//                noConnectionLayout.setVisibility(View.VISIBLE);
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
                Log.d("APPOINTMENTS: ", String.valueOf(response.body().toString().length()==2));
                if(response.body().toString().length()>2 && response.code() == 200){
                    consultations.addAll(response.body());
                    consultationsAdapter.notifyDataSetChanged();
                    noConsultations = false;
                    recentPatientsLayout.setVisibility(View.GONE);
                    consultationLayout.setVisibility(View.VISIBLE);
                    noPatientsLayout.setVisibility(View.GONE);
                    tvConsultations.setVisibility(View.GONE);
                }
                else if(response.body().toString().length()==2 && response.code() == 200){
                    recentPatientsLayout.setVisibility(View.GONE);
                    consultationLayout.setVisibility(View.GONE);
                    noPatientsLayout.setVisibility(View.VISIBLE);
                    tvConsultations.setVisibility(View.GONE);
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

    public URI loadUri(String socketURI){
        URI uri;
        try{
            uri = new URI(URLs.BASE_DOCTOR_URL+preferenceStorage.getUserId()+"/appointments/");
            return uri;
        }
        catch(URISyntaxException e){
            e.printStackTrace();
            return null;
        }
    }

    public void fetchAppointmentRequests(){
        URI uri = loadUri(URLs.BASE_DOCTOR_URL+preferenceStorage.getUserId()+"/appointments/");

        if(uri != null){
            mWebSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.i("DOCTOR_APPOINTMENT_SOCKET", "Opened");
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
                            Log.i("DOCTOR_APPOINTMENTS_MESSAGE", String.valueOf(message.length()));

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
            tvAppointmentsNumber.setText(String.valueOf(i++));
        }

    }

    private void addUpcomingAppointment(JSONObject appointment) {
        Appointment upcomingAppointment = null;
        try {
            Log.d("START_TIME", appointment.getString("start_time"));

            upcomingAppointment = new Appointment(
                    appointment.getString("title"),
                    appointment.getString("start_time"),
                    appointment.getString("end_time"),
                    appointment.getLong("doctor"),
                    appointment.getLong("client"),
                    appointment.getLong("id"),
                    appointment.getInt("status"),
                    appointment.getString("client_first_name"),
                    appointment.getString("client_last_name"),
                    appointment.getString("client_profile_image"),
                    false
            );

            upcomingAppointments.add(upcomingAppointment);
            appointmentsAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadAppointmentRequests(JSONArray appointmentsArray) throws JSONException {
        appointmentRequests.clear();

        for(int i=0; i < appointmentsArray.length(); i++){
            JSONObject appointmentObj = appointmentsArray.getJSONObject(i);
            addAppointmentRequest(appointmentObj);
            tvRequestsNumber.setText(String.valueOf(i++));

        }

        consultationLayout.setVisibility(View.VISIBLE);
        noPatientsLayout.setVisibility(View.GONE);
        tvConsultations.setVisibility(View.VISIBLE);
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
                    appointment.getString("symptoms"),
                    appointment.getString("client_first_name"),
                    appointment.getString("client_last_name"),
                    appointment.getString("client_profile_image")
            );

            appointmentRequests.add(appointmentRequest);

            for(AppointmentRequest aptRst : appointmentRequests){
                Log.d("ITEM_LIST", aptRst.getAbout());

            }

            appointmentRequestsAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addNotification(String message, int notificationType){
        //message="You have an appointment in the next 10 minutes"
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity())
                                                .setSmallIcon(R.drawable.icon_add)
                                                .setContentTitle("Appointment Alert")
                                                .setAutoCancel(true)
                                                .setContentText(message)
                                                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent notificationIntent = new Intent(requireActivity(), DoctorsSection.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("notification_message", message);

        PendingIntent pendingIntent = PendingIntent.getActivity(requireActivity(),
                                                     0,
                                                                notificationIntent,
                                                                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}