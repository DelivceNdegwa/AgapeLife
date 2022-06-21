package com.example.agapelife.adapters;

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
import com.example.agapelife.doctors.DoctorDetails;
import com.example.agapelife.R;
import com.example.agapelife.networking.pojos.DoctorResponse;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder>{

    Context context;
    List<DoctorResponse> doctors;

    public DoctorAdapter(Context context, List<DoctorResponse> doctors) {
        this.context = context;
        this.doctors = doctors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorResponse doctor = doctors.get(position);

        holder.tvDoctorName.setText("Dr "+doctor.getFirstName());
        holder.tvHospital.setText(doctor.getHospital());
        holder.tvDoctorSpecialization.setText(doctor.getSpeciality());

        Glide.with(context).
                load(doctor.getProfileImage())
                .placeholder(R.drawable.agape_life_logo_no_bg)
                .into(holder.profileImage);

        holder.id = doctor.getId();

    }

    @Override
    public int getItemCount() {
        return doctors.size();
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
