package com.example.agapelife.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.R;
import com.example.agapelife.instant_appointments.InstantPrescriptionDetails;
import com.example.agapelife.networking.pojos.Doctor;
import com.example.agapelife.networking.pojos.InstantMedicalReport;

import java.util.List;

public class InstantMedicalReportsAdapter extends RecyclerView.Adapter<InstantMedicalReportsAdapter.ViewHolder> {
    List<InstantMedicalReport> medicalReportList;
    Context context;

    public InstantMedicalReportsAdapter(List<InstantMedicalReport> medicalReportList, Context context) {
        this.medicalReportList = medicalReportList;
        this.context = context;

//        Log.d("InstantMedicalReportsAdapter", "Called with value:::"+String.valueOf(medicalReportList.get(0)));
    }


    @NonNull
    @Override
    public InstantMedicalReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instant_med_report_card, parent, false);
        return new InstantMedicalReportsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstantMedicalReportsAdapter.ViewHolder holder, int position) {
        InstantMedicalReport medicalReport = medicalReportList.get(position);
        Doctor prescribingDoctor = medicalReport.getDoctor();

        Log.d("onBindMedReport", medicalReport.getSuspectedIllness());
        holder.suspectedIllness = medicalReport.getSuspectedIllness();
        holder.prescriptionDate = medicalReport.getCreatedAt();
        holder.recommendation = medicalReport.getRecommendation();
        holder.prescription = medicalReport.getPrescription();

        holder.tvSuspectedIllness.setText("Suspected for "+medicalReport.getSuspectedIllness());
        holder.tvPrescDate.setText("On "+medicalReport.getCreatedAt());
        holder.tvPrescDoctor.setText("By Doctor "+prescribingDoctor.getFullName());

        holder.doctorName = prescribingDoctor.getFullName();
        holder.hospital = prescribingDoctor.getHospital();
        holder.doctorId = prescribingDoctor.getId();
    }

    @Override
    public int getItemCount() {
        return medicalReportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSuspectedIllness, tvPrescDoctor, tvPrescDate;
        ImageButton btnViewInstantReport;

        String doctorName, hospital, suspectedIllness, prescriptionDate, recommendation, prescription;
        int doctorId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSuspectedIllness = itemView.findViewById(R.id.tv_suspected_ilness);
            tvPrescDoctor = itemView.findViewById(R.id.tv_presc_doctor_name);
            tvPrescDate = itemView.findViewById(R.id.tv_presc_date);
            btnViewInstantReport = itemView.findViewById(R.id.btn_view_instant_med_report);

            btnViewInstantReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, InstantPrescriptionDetails.class);
                    intent.putExtra("DOCTOR_NAME", doctorName);
                    intent.putExtra("HOSPITAL", hospital);
                    intent.putExtra("DOCTOR_ID", doctorId);
                    intent.putExtra("SUSPECTED_ILLNESS", suspectedIllness);
                    intent.putExtra("RECOMMENDATION", recommendation);
                    intent.putExtra("PRESCRIPTION", prescription);
                    intent.putExtra("PRESC_DATE", prescriptionDate);
                    context.startActivity(intent);
                }
            });
        }
    }
}
