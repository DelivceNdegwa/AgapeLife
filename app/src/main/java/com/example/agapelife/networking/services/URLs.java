package com.example.agapelife.networking.services;

public class URLs {
//    public static final String API_URL = "http://192.168.100.126:8000/";
//    public static final String API_URL = "http://192.168.1.38:8000/";
    private static final String IP_ADDRESS = "192.168.100.188:8000";
    public static final String API_URL = "http://"+IP_ADDRESS+"/";
    public static final String SOCKET_URL = "ws://"+IP_ADDRESS+"/ws/socket-server";
    public static final String DOCTOR_SOCKET_URL = "ws://"+IP_ADDRESS+"/ws/doctor/";
    public static final String PATIENT_SOCKET_URL = "ws://"+IP_ADDRESS+"/ws/patient/";
//    public static final String API_URL = "http://192.168.100.150:8000/";
//    login/  login/refresh/  register/  doctor-register/
}
