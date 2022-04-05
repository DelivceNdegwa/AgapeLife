from django.shortcuts import render

from .serializers import AgapeUserTokenObtainPairSerializer, RegisterUserSerializer, RegisterDoctorSerializer
from staff.models import AgapeUser, Doctor

from rest_framework import generics
from rest_framework.permissions import AllowAny
from rest_framework_simplejwt.views import TokenObtainPairView


class AgapeUserObtainTokenPairView(TokenObtainPairView):
    permission_classes = (AllowAny,)
    serializer_class = AgapeUserTokenObtainPairSerializer


class AgapeUserRegisterView(generics.CreateAPIView):
    queryset = AgapeUser.objects.all()
    permission_classes = (AllowAny,)
    serializer_class = RegisterUserSerializer
    
    
class DoctorRegisterView(generics.CreateAPIView):
    queryset=Doctor.objects.all()
    permission_classes = (AllowAny,)
    serializer_class = RegisterDoctorSerializer
    
    
     
    
