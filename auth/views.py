from django.shortcuts import render

from .serializers import AgapeUserTokenObtainPairSerializer, RegisterSerializer
from staff.models import AgapeUser

from rest_framework import generics
from rest_framework.permissions import AllowAny
from rest_framework_simplejwt.views import TokenObtainPairView


class AgapeUserObtainTokenPairView(TokenObtainPairView):
    permission_classes = (AllowAny,)
    serializer_class = AgapeUserTokenObtainPairSerializer


class AgapeUserRegisterView(generics.CreateAPIView):
    queryset = AgapeUser.objects.all()
    permission_classes = (AllowAny,)
    serializer_class = RegisterSerializer
