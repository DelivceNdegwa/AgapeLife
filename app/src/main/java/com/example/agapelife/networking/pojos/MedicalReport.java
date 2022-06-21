package com.example.agapelife.networking.pojos;

import java.util.Objects;

public class MedicalReport {
    long appointmentId;
    String medication,doctorReport;

    public MedicalReport() {
    }

    public MedicalReport(long appointmentId, String medication, String doctorReport) {
        this.appointmentId = appointmentId;
        this.medication = medication;
        this.doctorReport = doctorReport;
    }

    public long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDoctorReport() {
        return doctorReport;
    }

    public void setDoctorReport(String doctorReport) {
        this.doctorReport = doctorReport;
    }


}
