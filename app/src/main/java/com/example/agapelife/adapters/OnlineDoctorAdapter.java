package com.example.agapelife.adapters;

import static com.example.agapelife.networking.services.URLs.API_URL;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agapelife.DoctorDetails;
import com.example.agapelife.R;
import com.example.agapelife.models.OnlineDoctor;

import java.util.ArrayList;
import java.util.List;

public class OnlineDoctorAdapter extends RecyclerView.Adapter<OnlineDoctorAdapter.ViewHolder> {
    List<OnlineDoctor> onlineDoctorList;
    Context context;

    public OnlineDoctorAdapter(Context context, List<OnlineDoctor> onlineDoctorList) {
        this.onlineDoctorList = onlineDoctorList;
        this.context = context;
    }


    @NonNull
    @Override
    public OnlineDoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new OnlineDoctorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineDoctorAdapter.ViewHolder holder, int position) {
        OnlineDoctor doctor = onlineDoctorList.get(position);
        holder.tvDoctorName.setText("Dr "+doctor.getFirstName());
        holder.tvHospital.setText(doctor.getHospital());
        holder.tvDoctorSpecialization.setText(doctor.getSpecialization());

        Glide.with(context).
                load(API_URL+doctor.getProfileImage())
                .placeholder(R.drawable.agape_life_logo_no_bg)
                .into(holder.profileImage);

        holder.id = doctor.getIdNumber();
    }

    @Override
    public int getItemCount() {
        return onlineDoctorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView tvDoctorName, tvDoctorSpecialization, tvHospital;
        long id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.img_profile);
            tvDoctorName = itemView.findViewById(R.id.tv_doc_name);
            tvDoctorSpecialization = itemView.findViewById(R.id.tv_speciality);
            tvHospital = itemView.findViewById(R.id.tv_hospital);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DoctorDetails.class);
                    intent.putExtra("DOCTOR_ID", id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
