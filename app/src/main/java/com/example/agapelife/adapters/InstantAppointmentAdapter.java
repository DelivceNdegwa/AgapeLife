package com.example.agapelife.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.networking.pojos.DoctorInstantAppointments;

import java.util.List;

public class InstantAppointmentAdapter extends RecyclerView.Adapter<InstantAppointmentAdapter.ViewHolder> {
    Context context;
    List<DoctorInstantAppointments> instantAppointmentsList;

    public InstantAppointmentAdapter(Context context, List<DoctorInstantAppointments> instantAppointmentsList) {
        this.context = context;
        this.instantAppointmentsList = instantAppointmentsList;
    }

    @NonNull
    @Override
    public InstantAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InstantAppointmentAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return instantAppointmentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
