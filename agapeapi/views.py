from django.shortcuts import render
from staff.models import *
from .serializers import *

from django.http import Http404
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt


from django.core.exceptions import ObjectDoesNotExist

from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.generics import ListCreateAPIView, RetrieveUpdateAPIView, ListAPIView, RetrieveUpdateDestroyAPIView, RetrieveAPIView, UpdateAPIView, CreateAPIView

from agora_tokens.RtcGenerateToken import TokenGenerator

import datetime
from datetime import timedelta

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
    
    
@csrf_exempt
@api_view(["PUT"])
@permission_classes((AllowAny,))
def editOnlineStatus(request, id_number):
    try:
        doctor = Doctor.objects.filter(id_number=id_number).first()
        
        fetched_status = request.POST.get('is_available')
        
        if fetched_status == 'true':
            online_status = True
        else:
            online_status = False

        doctor.is_available = online_status
        doctor.save()
        
        message = "Status changed successfully"
        
        return Response({'message':message}, status=status.HTTP_200_OK)
            
    except ObjectDoesNotExist as e:
        message = str(e)
        return Response({'message':message}, status=status.HTTP_400_BAD_REQUEST)

    
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
        appointments = Appointment.objects.select_related('doctor').filter(doctor=doctor, status=Appointment.PENDING)
        return appointments
    
class DoctorDetailsView(RetrieveUpdateAPIView):
    serializer_class = DoctorSerializer
    
    def get_object(self):
        id_number = self.kwargs.get('id_number', None)
        doctor = Doctor.objects.filter(id_number=id_number).first()
        return doctor
    

class DoctorPatientsView(ListAPIView):
    serializer_class = AgapeUserSerializer
    
    def get_queryset(self):
        id_number = self.kwargs.get('id_number', None)
        doctor = Doctor.objects.filter(id_number=id_number).first()
        appointment = Appointment.objects.select_related('doctor').filter(doctor=doctor)
    
        patients = doctor.appointment_set.all()
        return patients
        
    
    
class AgapeUserDetailView(RetrieveUpdateAPIView):
    serializer_class = AgapeUserSerializer
    
    def get_object(self):
        id_number = self.kwargs.get('id_number', None)
        user = AgapeUser.objects.filter(id_number=id_number).first()
        return user
    
    
class AppointmentDetailView(RetrieveUpdateAPIView):
    queryset = Appointment.objects.all()
    serializer_class = AppointmentSerializer


class AppointmentRequestListView(ListAPIView):
    # queryset = AppointmentRequest
    serializer_class = AppointmentRequestSerializer
    def get_queryset(self):
        id_number = self.kwargs.get('id_number', None)
        doctor = Doctor.objects.filter(id_number=id_number).first()
        appointment_requests = AppointmentRequest.objects.filter(doctor__in=doctor)
        
        return appointment_requests
    
    
class AppointmentRequestDetailView(RetrieveUpdateAPIView):
    queryset = AppointmentRequest
    serializer_class = AppointmentRequestSerializer
    
    
class DoctorPrescriptionsDetailView(RetrieveUpdateAPIView):
    queryset = DoctorPrescription
    serializer_class = DoctorPrescriptionSerializer
    
class DoctorPrescriptionListView(ListAPIView):
    queryset = DoctorPrescription
    serializer_class = DoctorPrescriptionSerializer
    
    
class PatientsSymptomsDetailView(RetrieveUpdateAPIView):
    queryset = PatientSymptoms
    serializer_class = PatientSymptomsSerializer
    

@csrf_exempt
@api_view(["GET"])
def generateTokens(request, pk):
    # this is the id for the meeting 
    appointment = Appointment.objects.filter(id=pk).first()
    
    channel_name = appointment.title
    start_time = appointment.start_time
    end_time = appointment.end_time
    uid = appointment.client.username+"_"+str(appointment.client.id)
    
    time_delta = end_time - start_time
    
    try:
        token_generator = TokenGenerator(uid, channel_name, start_time, int(time_delta.total_seconds()))
        token = token_generator.generate()
        return Response({'token': token}, status=status.HTTP_200_OK)
    except Exception as e:
        return Response({'error':str(e)}, status=status.HTTP_400_BAD_REQUEST)


@csrf_exempt
@api_view(["POST"])
@permission_classes((AllowAny,))
def createAppointment(request):
    appointment_title = request.POST.get("appointment_title")
    start_time = request.POST.get("start_time")
    end_time = request.POST.get("end_time")
    doctor_id = request.POST.get("doctor_id")
    client_id = request.POST.get("patient_id")
    
    appointment = Appointment(
            title = appointment_title,
            start_time = start_time,
            end_time = end_time,
            client = AgapeUser.objects.filter(id=client_id).first(),
            doctor = Doctor.objects.filter(id=doctor_id).first()
        )
    appointment.save()
        
    return Response({"success":True, "message":"Appointment created successfully", "error":None}, status=status.HTTP_201_CREATED)
    

@csrf_exempt
@api_view(["POST"])
@permission_classes((AllowAny,))
def bookAppointment(request):
    client = int(request.POST.get("client_id"))
    doctor = request.POST.get("doctor_id")
    about = request.POST.get("about")
    symptoms = request.POST.get("patient_symptoms")
    
    patient_symptoms = PatientSymptoms()
    patient_symptoms.symptoms = symptoms
    patient_symptoms.patient =AgapeUser.objects.filter(id=client).first()
    patient_symptoms.save()
    
    appointment_request = AppointmentRequest()
    appointment_request.client=AgapeUser.objects.filter(id=client).first()
    appointment_request.doctor=Doctor.objects.filter(id=doctor).first()
    appointment_request.about = about
    appointment_request.symptoms = patient_symptoms
    appointment_request.save()
    
    return Response({"success":True, "message":"Appointment has been booked successfully", "error":None}, status=status.HTTP_201_CREATED)
