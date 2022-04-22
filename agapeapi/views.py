from django.shortcuts import render
from staff.models import *
from .serializers import *

from django.http import Http404

from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.generics import ListCreateAPIView, RetrieveUpdateAPIView, ListAPIView, RetrieveUpdateDestroyAPIView, RetrieveAPIView, UpdateAPIView, CreateAPIView

'''' 
Data the application should have
    MedicalCategory Data: READ(App can only read the data)
    AgapeUser Data: READ, WRITE, UPDATE(AgapeUsers only can create, update and read their details)
    Doctor Data: READ, WRITE, UPDATE(By Doctors only)
    MedicalTips Data: READ
    User Feedback:CREATE
    DoctorCategory Pivot: READ, UPDATE, DELETE(for verified Doctors only)
'''



# @api_view(['GET'])
# @permission_classes([AllowAny])

# def getMedicalCategory(request):

class MedicalCategoryListView(ListCreateAPIView):
    queryset = MedicalCategory.objects.all()
    serializer_class = MedicalCategorySerializer


class MedicalCategoryDetailsView(APIView):
    def get_object(self, pk):
        try:
            MedicalCategory.objects.filter(pk=pk).filter()
        except MedicalCategory.DoesNotExist:
            raise Http404
            
    def get(self, request, pk, format=None):   
        medical_category = MedicalCategory.objects.filter(pk=pk).first()
        serializer = MedicalCategorySerializer(medical_category)
        return Response(serializer.data)
    

class AgapeUserListView(ListCreateAPIView):
    queryset = AgapeUser.objects.all()
    serializer_class = AgapeUserSerializer

    
class DoctorListView(ListAPIView):
    # permission_classes = (IsAuthenticated, )
    queryset = Doctor.objects.filter(is_verified=True)
    serializer_class = DoctorSerializer
    
    
class DoctorDetailsView(RetrieveUpdateAPIView):
    queryset = Doctor.objects.filter(is_verified=True)
    serializer_class = DoctorSerializer


class UserFeedBackListView(ListCreateAPIView):
    queryset = UserFeedback.objects.all()
    serializer_class = UserFeedbackSerializer
    

class UserFeedBackDetailView(RetrieveUpdateAPIView):
    queryset = UserFeedback.objects.all()
    serializer_class = UserFeedbackSerializer
    
    
    
class MedicalTipsListView(ListCreateAPIView):
    queryset = MedicalTips.objects.all()
    serializer_class = MedicalTipsSerializer
    

class MedicalTipsDetailView(RetrieveAPIView):
    queryset = MedicalTips.objects.all()
    serializer_class = MedicalTipsSerializer
    
    
class AppointmentListView(ListCreateAPIView):
    queryset = Appointment.objects.all()
    serializer_class = AppointmentSerializer
    
    
class UserAppointmentsListView(RetrieveAPIView):
    serializer_class = AppointmentSerializer
    
    def get_queryset(self):
        client = AgapeUser.objects.filter(id=id).first()
        appointments = Appointment.objects.select_related('client').filter(client=client)
        return appointments
    

class DoctorAppointmentsListView(ListAPIView):
    serializer_class = AppointmentSerializer
    
    def get_queryset(self):
        pk = self.kwargs.get('pk')
        doctor = Doctor.objects.filter(id=pk).first()
        appointments = Appointment.objects.select_related('doctor').filter(doctor=doctor)
        return appointments
    
class DoctorDetailsView(RetrieveUpdateAPIView):
    serializer_class = DoctorSerializer
    
    def get_object(self):
        id_number = self.kwargs.get('id_number', None)
        doctor = Doctor.objects.filter(id_number=id_number).first()
        return doctor
    
    
class AgapeUserDetailView(RetrieveUpdateAPIView):
    serializer_class = AgapeUserSerializer
    
    def get_object(self):
        id_number = self.kwargs.get('id_number', None)
        user = AgapeUser.objects.filter(id_number=id_number).first()
        return user
    
    
class AppointmentDetailView(RetrieveUpdateAPIView):
    queryset = Appointment.objects.all()
    serializer_class = AppointmentSerializer

