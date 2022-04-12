from django.shortcuts import render

from .serializers import AgapeUserTokenObtainPairSerializer, RegisterUserSerializer, RegisterDoctorSerializer
from staff.models import AgapeUser, Doctor

from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt

from rest_framework import generics
from rest_framework.permissions import AllowAny
from rest_framework_simplejwt.views import TokenObtainPairView

from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status


class AgapeUserObtainTokenPairView(TokenObtainPairView):
    permission_classes = (AllowAny,)
    serializer_class = AgapeUserTokenObtainPairSerializer
    
class AgapeUserRegisterView(APIView):
    # queryset = AgapeUser.objects.all()
    # permission_classes = (AllowAny,)
    # serializer_class = RegisterUserSerializer
    @method_decorator(csrf_exempt)
    def post(self, request, format=None):
            serializer = RegisterUserSerializer(data=request.data)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data, status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

class DoctorRegisterView(APIView):
    # queryset=Doctor.objects.all()
    # permission_classes = (AllowAny,)
    # serializer_class = RegisterDoctorSerializer
    @method_decorator(csrf_exempt)
    def post(self, request, format=None):
            serializer = RegisterDoctorSerializer(data=request.data)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data, status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    
     
    
