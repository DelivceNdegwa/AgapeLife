package com.example.agapelife.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agapelife.R;
import com.example.agapelife.medical_reports.DoctorReportDetailsActivity;
import com.example.agapelife.networking.pojos.MedicalReportResponse;

import java.util.List;

public class MedicalReportAdapter extends RecyclerView.Adapter<MedicalReportAdapter.ViewHolder> {
    List<MedicalReportResponse> patientsReports;
    Context context;

    public MedicalReportAdapter(List<MedicalReportResponse> patientsReports, Context context) {
        this.patientsReports = patientsReports;
        this.context = context;
    }


    @NonNull
    @Override
    public MedicalReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalReportAdapter.ViewHolder holder, int position) {
        MedicalReportResponse patientResponse = patientsReports.get(position);
        holder.appointmentId = patientResponse.getAppointmentId();
        holder.doctorId = patientResponse.getDoctorId();

        holder.doctorName = patientResponse.getDoctorName();
        holder.doctorMedication = patientResponse.getMedication();
        holder.createdAt = patientResponse.getCreatedAt();
        holder.doctorReport = patientResponse.getDoctorReport();
        holder.appointmentTitle = patientResponse.getAppointmentTitle();

        holder.tvDoctorName.setText(holder.doctorName);
        holder.tvReportDate.setText(holder.createdAt);

    }

    @Override
    public int getItemCount() {
        return patientsReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        long appointmentId, doctorId;
        String doctorName, appointmentTitle,doctorMedication, createdAt, doctorReport;

        TextView tvDoctorName, tvReportDate;
        ImageView btnViewReport;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReportDate = itemView.findViewById(R.id.tv_report_date);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            btnViewReport = itemView.findViewById(R.id.btn_view_report);

            btnViewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DoctorReportDetailsActivity.class);

                    intent.putExtra("DOCTOR_NAME", doctorName);
                    intent.putExtra("DOCTOR_MEDICATION", doctorMedication);
                    intent.putExtra("DOCTOR_REPORT", doctorReport);
                    intent.putExtra("CREATED_AT", createdAt);
                    intent.putExtra("APPOINTMENT_ID", appointmentId);
                    intent.putExtra("DOCTOR_ID", doctorId);
                    intent.putExtra("APPOINTMENT_TITLE", appointmentTitle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
