package com.example.agapelife.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agapelife.appointments.AppointmentRequestDetailsActivity;
import com.example.agapelife.R;
import com.example.agapelife.models.AppointmentRequest;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.DoctorResponse;
import com.example.agapelife.networking.services.ServiceGenerator;
import com.example.agapelife.utils.PreferenceStorage;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRequestsAdapter extends RecyclerView.Adapter<AppointmentRequestsAdapter.ViewHolder> {
    Context context;
    List<AppointmentRequest> appointmentsSocket;
    DoctorResponse doctor;
    AgapeUserResponse client;
    PreferenceStorage preferenceStorage;

    public AppointmentRequestsAdapter(Context context, List<AppointmentRequest>  appointmentsSocket) {
        this.context = context;
        this.appointmentsSocket = appointmentsSocket;

    }

    @NonNull
    @Override
    public AppointmentRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation, parent, false);
        return new AppointmentRequestsAdapter.ViewHolder(view);
    }

    private DoctorResponse getDoctorDetails(long doctorId) {
        Call<DoctorResponse> call = ServiceGenerator.getInstance().getApiConnector().getDoctorDetails(doctorId);
        call.enqueue(new Callback<DoctorResponse>() {
            @Override
            public void onResponse(Call<DoctorResponse> call, Response<DoctorResponse> response) {
                if(response.code() == 200 && response.body() != null){
                    doctor = response.body();
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("onResponseErrorCode", String.valueOf(response.code()));
                    Log.d("onResponseErrorBody", String.valueOf(response.body()));
                    doctor = null;
                }
            }

            @Override
            public void onFailure(Call<DoctorResponse> call, Throwable t) {
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                Log.d("onFailureThrowable", t.getMessage());
                Log.d("onFailureStackTrace", Arrays.toString(t.getStackTrace()));
                doctor = null;
            }
        });

        return doctor;
    }

    private AgapeUserResponse getClientDetails(long clientId) {
        Log.d("CLIENT_", "Im called");
        Call<AgapeUserResponse> call = ServiceGenerator.getInstance().getApiConnector().getPatientDetails(clientId);
        call.enqueue(new Callback<AgapeUserResponse>() {
            @Override
            public void onResponse(Call<AgapeUserResponse> call, Response<AgapeUserResponse> response) {
               client = response.body();
               Log.d("CLIENT_RESPONSE", String.valueOf(client));
            }

            @Override
            public void onFailure(Call<AgapeUserResponse> call, Throwable t) {
                Log.d("CLIENT_RESPONSE", "Something went wrong");
            }
        });
        return client;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentRequestsAdapter.ViewHolder holder, int position) {
        AppointmentRequest consultation = appointmentsSocket.get(position);
        holder.clientName = consultation.getClientFirstName() +" "+ consultation.getClientLastName();
        holder.symptoms = consultation.getSymptoms();
        holder.about = consultation.getAbout();
        holder.appointmentId = (int) consultation.getId();
//        Log.d("CLIENT_ID", String.valueOf(consultation.getClientId()));
        holder.clientId = (int) consultation.getClientId();
        holder.doctorId = (int) consultation.getDoctorId();
        holder.clientFirstName.setText(consultation.getClientFirstName());
        holder.clientLastName.setText(consultation.getClientLastName());

        Glide.with(context).
                load(consultation)
                .placeholder(R.drawable.agape_life_logo_no_bg)
                .into(holder.clientPhoto);
    }

    @Override
    public int getItemCount() {
        return appointmentsSocket.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int clientId, doctorId, appointmentId;
        long clientIdNumber;
        AgapeUserResponse client;
        ImageView clientPhoto;
        TextView appointmentDate, appointmentTime, clientFirstName, clientLastName;
        Button btnViewAppointment;

        String about, symptoms, clientName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clientPhoto = itemView.findViewById(R.id.client_photo);
            appointmentDate = itemView.findViewById(R.id.tv_appointment_date);
            appointmentTime = itemView.findViewById(R.id.tv_upcoming_time);
            btnViewAppointment = itemView.findViewById(R.id.btn_view_appointment);
            clientFirstName = itemView.findViewById(R.id.tv_first_name);
            clientLastName = itemView.findViewById(R.id.tv_last_name);

            btnViewAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AppointmentRequestDetailsActivity.class);
                    intent.putExtra("ABOUT", about);
                    intent.putExtra("SYMPTOMS", symptoms);
                    intent.putExtra("CLIENT_ID", clientId);
                    intent.putExtra("DOCTOR_ID", doctorId);
                    intent.putExtra("PATIENT_NAME", clientName);
                    intent.putExtra("APPOINTMENT_ID", appointmentId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
