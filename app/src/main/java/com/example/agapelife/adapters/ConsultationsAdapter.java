package com.example.agapelife.adapters;

import android.content.Context;
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
import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.AgapeUserResponse;
import com.example.agapelife.networking.pojos.AppointmentResponse;
import com.example.agapelife.networking.services.ServiceGenerator;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultationsAdapter extends RecyclerView.Adapter<ConsultationsAdapter.ViewHolder> {
    Context context;
    List<AppointmentResponse> consultations;
    AgapeUserResponse client;

    public ConsultationsAdapter(Context context, List<AppointmentResponse> consultations) {
        this.context = context;
        this.consultations = consultations;
    }


    @NonNull
    @Override
    public ConsultationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation, parent, false);
        return new ConsultationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultationsAdapter.ViewHolder holder, int position) {
        AppointmentResponse consultation = consultations.get(position);
        holder.clientId = consultation.getClient();
        holder.client = getClientDetails(holder.clientId);
        holder.clientFirstName.setText(holder.client.getFirstName());
        holder.clientLastName.setText(holder.client.getLastName());
        Glide.with(context).
                load(holder.client.getProfilePhoto())
                .placeholder(R.drawable.agape_life_logo_no_bg)
                .into(holder.clientPhoto);
    }

    private AgapeUserResponse getClientDetails(long clientId) {
        Call<AgapeUserResponse> call = ServiceGenerator.getInstance().getApiConnector().getUserDetails(clientId);
        call.enqueue(new Callback<AgapeUserResponse>() {

            @Override
            public void onResponse(Call<AgapeUserResponse> call, Response<AgapeUserResponse> response) {
                if(response.code() == 200 && response.body() != null){
                    client = response.body();
                }
                else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d("onResponseErrorCode", String.valueOf(response.code()));
                    Log.d("onResponseErrorBody", String.valueOf(response.body()));
                    client = null;
                }
            }

            @Override
            public void onFailure(Call<AgapeUserResponse> call, Throwable t) {
                Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                Log.d("onFailureThrowable", t.getMessage());
                Log.d("onFailureStackTrace", Arrays.toString(t.getStackTrace()));
                client = null;
            }
        }
        );
        return client;
    }

    @Override
    public int getItemCount() {
        return consultations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        long clientId;
        AgapeUserResponse client;
        ImageView clientPhoto;
        TextView appointmentDate, appointmentTime, clientFirstName, clientLastName;
        Button btnViewAppointment;
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
                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
