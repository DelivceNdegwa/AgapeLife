package com.example.agapelife.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.method.CharacterPickerDialog;
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
import com.example.agapelife.models.Doctor;
import com.example.agapelife.networking.pojos.DoctorRequest;
import com.example.agapelife.networking.pojos.DoctorResponse;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    Context context;
    List<Doctor> doctorRequestList = new ArrayList<>();

    public DoctorAdapter(Context context, List<Doctor> doctorRequestList) {
        this.context = context;
        this.doctorRequestList = doctorRequestList;
    }

    @NonNull
    @Override
    public DoctorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAdapter.ViewHolder holder, int position) {
        Doctor doctor = doctorRequestList.get(position);
        holder.id = doctor.getId();
        holder.hospitalName.setText(doctor.getHospital());
        holder.docSpecialization.setText(doctor.getSpeciality());
        holder.docName.setText(String.format("Dr %s", doctor.getFirstName()));
        Glide.with(context).load(doctor.getProfileImage()).placeholder(R.drawable.docor_img).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void updateDoctorsAdapter(List<Doctor> doctorRequestList){
        this.doctorRequestList = doctorRequestList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Integer id;
        ImageView profileImage;
        TextView docName, docSpecialization, hospitalName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.tv_doc_name);
            docSpecialization = itemView.findViewById(R.id.tv_speciality);
            hospitalName = itemView.findViewById(R.id.tv_hospital);
            profileImage = itemView.findViewById(R.id.img_profile);

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
