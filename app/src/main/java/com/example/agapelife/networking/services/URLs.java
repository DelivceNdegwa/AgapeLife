package com.example.agapelife.networking.services;

public class URLs {
//    private static final String IP_ADDRESS = "192.168.100.188:8000";
    private static final String IP_ADDRESS = "192.168.100.203:8000";

//    API URLS
    public static final String API_URL = "http://"+IP_ADDRESS+"/";


//    SOCKET URLS
    public static final String ONLINE_DOCTORS = "ws://"+IP_ADDRESS+"/ws/online-doctors/";
    public static final String BASE_DOCTOR_URL = "ws://"+IP_ADDRESS+"/ws/doctor/";
    public static final String BASE_PATIENT_URL = "ws://"+IP_ADDRESS+"/ws/patient/";

}
