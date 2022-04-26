package com.example.agapelife.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.AgapeUserResponse;

import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {
    Context context;
    List<AgapeUserResponse> patients;

    public PatientsAdapter(Context context, List<AgapeUserResponse> patients) {
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    public PatientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new PatientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsAdapter.ViewHolder holder, int position) {
        AgapeUserResponse patient = patients.get(position);
        String patientInitials = patient.getFirstName().charAt(0)+String.valueOf(patient.getLastName().charAt(0));
        holder.tvPatientInitials.setText(patientInitials);

        if(!patient.getProfilePhoto().isEmpty()){
            holder.withProfileLayout.setVisibility(View.VISIBLE);
            holder.withoutProfileLayout.setVisibility(View.GONE);

            Glide.with(context)
                    .load(patient.getProfilePhoto())
                    .into(holder.ivPatientProfileImage);
        }

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPatientInitials;
        ImageView ivPatientProfileImage;
        ConstraintLayout withProfileLayout, withoutProfileLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPatientInitials = itemView.findViewById(R.id.tv_patient_intials);
            ivPatientProfileImage = itemView.findViewById(R.id.iv_profile);
            withProfileLayout = itemView.findViewById(R.id.with_profile_pic);
            withoutProfileLayout = itemView.findViewById(R.id.no_profile_pic);
        }
    }
}
