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

import com.bumptech.glide.Glide;
import com.example.agapelife.appointments.AppointmentDetailsActivity;
import com.example.agapelife.appointments.AppointmentJoiningRoomActivity;
import com.example.agapelife.R;
import com.example.agapelife.models.Appointment;
import com.example.agapelife.utils.PreferenceStorage;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {
    Context context;
    List<Appointment> upcomingAppointments;
    PreferenceStorage preferenceStorage;


    public AppointmentsAdapter(Context context, List<Appointment> upcomingAppointments, PreferenceStorage preferenceStorage) {
        this.context = context;
        this.upcomingAppointments = upcomingAppointments;
        this.preferenceStorage = preferenceStorage;
    }

    @NonNull
    @Override
    public AppointmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation, parent, false);
        return new AppointmentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsAdapter.ViewHolder holder, int position) {
        Appointment appointment = upcomingAppointments.get(position);
        String appointmentDate = appointment.getStartTime();

        Timestamp appointmentTimeStamp = Timestamp.valueOf(appointmentDate);
        Date date = new Date(appointmentTimeStamp.getTime());

        LocalDate localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            int year  = localDate.getYear();
            int month = localDate.getMonthValue();
            String day = "";

            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            String time = localDateFormat.format(date);

            LocalDate today = LocalDate.now();

            if(today.isEqual(localDate)){
                day = "Today";
            }
            else if(today.isBefore(localDate)){
                day = "Tommorow";
            }
            else{
                day = localDate.getDayOfWeek().name().toLowerCase(Locale.ROOT);
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

            // Fetch Appointment details

            holder.appointmentId = appointment.getId();
            holder.appointmentTitle = appointment.getAppointmentTitle();


            if(day != "Today" || day != "Tommorow"){
                holder.strAppointmentDate = day+" "+dateFormat.format(date);
            }
            else{
                holder.strAppointmentDate = day;
            }


            holder.strAppointmentTime = time;
            holder.appointmentDate.setText(day);
            holder.appointmentTime.setText(time);

            Glide.with(context).
                    load(appointment.getProfileImage())
                    .placeholder(R.drawable.agape_life_logo_no_bg)
                    .into(holder.userPhoto);

            if(preferenceStorage.isDoctor()){
                holder.userId = appointment.getPatientId();
                holder.fullName = appointment.getFirstName()+" "+appointment.getLastName();
                holder.userFirstName.setText(appointment.getFirstName());
                holder.userLastName.setText(appointment.getLastName());
                holder.appointmentAbout = appointment.getFirstName()+"'s medical checkup on "+ appointment.getAppointmentTitle();
            }
            else{
                holder.userId = appointment.getDoctorId();
                holder.fullName = "Doctor "+appointment.getFirstName();
                holder.userFirstName.setText("Doctor ");
                holder.userLastName.setText(appointment.getFirstName());
                holder.appointmentAbout = "Your medical checkup request on "+ appointment.getAppointmentTitle();
            }
        }

    }

    @Override
    public int getItemCount() {
        return upcomingAppointments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        long userId, appointmentId;
        String strAppointmentDate, strAppointmentTime, fullName, appointmentTitle, appointmentAbout;

        ImageView userPhoto;
        TextView appointmentDate, appointmentTime, userFirstName, userLastName;
        Button btnViewAppointment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.client_photo);
            appointmentDate = itemView.findViewById(R.id.tv_appointment_date);
            appointmentTime = itemView.findViewById(R.id.tv_upcoming_time);
            btnViewAppointment = itemView.findViewById(R.id.btn_view_appointment);
            userFirstName = itemView.findViewById(R.id.tv_first_name);
            userLastName = itemView.findViewById(R.id.tv_last_name);


            btnViewAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AppointmentDetailsActivity.class);
//                    intent.putExtra("USER_ID", userId);
//                    intent.putExtra("APPOINTMENT_ID", appointmentId);
//                    intent.putExtra("APPOINTMENT_DATE", strAppointmentDate);
//                    intent.putExtra("APPOINTMENT_TIME", strAppointmentTime);
//                    intent.putExtra("FULL_NAME", fullName);
//                    context.startActivity(intent);

                    Intent i = new Intent(context, AppointmentJoiningRoomActivity.class);
                    i.putExtra("APPOINTMENT_TITLE", appointmentTitle);
                    i.putExtra("APPOINTMENT_ABOUT", appointmentAbout);
                    i.putExtra("APPOINTMENT_DATE", strAppointmentDate);
                    i.putExtra("APPOINTMENT_TIME", strAppointmentTime);
                    i.putExtra("FULL_NAME", fullName);
                    i.putExtra("APPOINTMENT_ID", appointmentId);

                    context.startActivity(i);
                }
            });
        }
    }
}
