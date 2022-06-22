import json
import datetime
from datetime import timedelta
from os import stat
from re import S

from django.http import Http404, JsonResponse
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import render
from django.core.exceptions import ObjectDoesNotExist

from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from rest_framework.views import APIView
from rest_framework.generics import ListCreateAPIView, RetrieveUpdateAPIView, ListAPIView, RetrieveUpdateDestroyAPIView, RetrieveAPIView, UpdateAPIView, CreateAPIView

from agora_tokens.RtcGenerateToken import TokenGenerator
from django_celery_beat.models import PeriodicTask, CrontabSchedule

from staff.models import *
from .serializers import *

from django_celery_beat.models import PeriodicTask, IntervalSchedule



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
        pk = self.kwargs.get('pk')
        client = AgapeUser.objects.filter(id=pk).first()
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


class DoctorDetailsIDView(RetrieveUpdateAPIView):
    serializer_class = DoctorSerializer
    
    def get_object(self):
        id = self.kwargs.get('id', None)
        doctor = Doctor.objects.filter(id=id).first()
        return doctor
    

class DoctorPatientsView(ListAPIView):
    serializer_class = AgapeUserSerializer
    
    def get_queryset(self):
        id_number = self.kwargs.get('id_number', None)
        doctor = Doctor.objects.filter(id_number=id_number).first()
        appointment = Appointment.objects.select_related('doctor').filter(doctor=doctor)
    
        
        return appointment.appointments.all()
        
    
    
class AgapeUserDetailView(RetrieveUpdateAPIView):
    serializer_class = AgapeUserSerializer
    
    def get_object(self):
        id_number = self.kwargs.get('id_number', None)
        user = AgapeUser.objects.filter(id_number=id_number).first()
        return user


class ClientDetailView(RetrieveUpdateAPIView):
    serializer_class = AgapeUserSerializer
    
    def get_object(self):
        id = int(self.kwargs.get('pk', None))
        print("USER_ID:",id)
        patient = AgapeUser.objects.filter(id=id).first()
        print("USER:", patient)
        print(patient)
        return patient
    

class PatientDetailView(RetrieveUpdateAPIView):
    serializer_class = AgapeUserSerializer
    
    def get_object(self):
        id = self.kwargs.get('id', None)
        print("USER_ID:",id)
        user = AgapeUser.objects.filter(id_number=id).first()
        print("USER:", user)
        print(user.first_name)
        return user

    
@csrf_exempt
@api_view(["POST"])
@permission_classes((AllowAny,))  
def getClientDetails(request, pk):
    user = AgapeUser.objects.filter(id=pk).first()
    
    data = {
        'id': user.id,
        'first_name': user.first_name,
        'last_name': user.last_name,
        'profile_image': user.profile_image    
    }
    
    return JsonResponse(data, status=status.HTTP_200_OK)
    
    
    
class AppointmentDetailView(RetrieveUpdateAPIView):
    queryset = Appointment.objects.all()
    serializer_class = AppointmentSerializer


class AppointmentRequestListView(ListAPIView):
    # queryset = AppointmentRequest
    serializer_class = AppointmentRequestSerializer
    def get_queryset(self):
        id_number = self.kwargs.get('id_number', None)
        doctor = Doctor.objects.filter(id_number=id_number).first()
        print(str(doctor.id))
        appointment_requests = AppointmentRequest.objects.filter(doctor=doctor)
        
        return appointment_requests
    
    
class AppointmentRequestDetailView(RetrieveUpdateAPIView):
    queryset = AppointmentRequest
    serializer_class = AppointmentRequestSerializer
    
    
class MedicalReportsDetailView(RetrieveUpdateAPIView):
    queryset = MedicalReport
    serializer_class = MedicalReportSerializer
    
class MedicalReportListView(ListAPIView):
    queryset = MedicalReport
    serializer_class = MedicalReportSerializer
    
    
class PatientsSymptomsDetailView(RetrieveUpdateAPIView):
    queryset = PatientSymptoms
    serializer_class = PatientSymptomsSerializer
    

@csrf_exempt
@api_view(["GET"])
def generateClientTokens(request, pk):
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
        print("CLIENT_TOKEN: ",token)
        print("CHANNEL_NAME: ", channel_name)
        data = {
            'token': token,
            'channel_name': channel_name
            }
        return Response(data, status=status.HTTP_200_OK)
    except Exception as e:
        return Response({'error':str(e)}, status=status.HTTP_400_BAD_REQUEST)



@csrf_exempt
@api_view(["GET"])
def generateDoctorTokens(request, pk):
    # this is the id for the meeting 
    appointment = Appointment.objects.filter(id=pk).first()
    
    channel_name = appointment.title
    start_time = appointment.start_time
    end_time = appointment.end_time
    
    uid = appointment.doctor.username+"_"+str(appointment.doctor.id)
    time_delta = end_time - start_time
    print("DOCTOR_UID:", uid)
    print("CHANNEL_NAME: ", channel_name)
    try:
        token_generator = TokenGenerator(uid, channel_name, start_time, int(time_delta.total_seconds()))
        token = token_generator.generate()
        print("DOCTOR_TOKEN: ",token)
        
        data = {
            'token': token,
            'channel_name': channel_name
            }
        
        return Response(data, status=status.HTTP_200_OK)
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
    request_appointment_id = int(request.POST.get("appointment_id"))
    
    client = AgapeUser.objects.filter(id=client_id).first()
    doctor = Doctor.objects.filter(id=doctor_id).first()
    
    
    appointment = Appointment(
            title = appointment_title,
            about = appointment_title,
            start_time = start_time,
            end_time = end_time,
            client = client,
            doctor = doctor,
            client_first_name = client.first_name,
            client_last_name = client.last_name,
            client_profile_image = client.profile_image,
            doctor_first_name = doctor.first_name,
            doctor_last_name = doctor.last_name,
            doctor_profile_image = doctor.profile_image
        )
    appointment.save()
    
    appointment_request = AppointmentRequest.objects.filter(id=request_appointment_id).first()
    print("REQUEST_APT", appointment_request)
    
    appointment_request.status = AppointmentRequest.APPROVED
    appointment_request.read = True
    appointment_request.save()
    
    doctor_message = 'Hello Doctor {}, your appointment with {} {} starts in 5 minutes'.format(doctor.first_name, client.first_name, client.last_name)
    patient_message= 'Hello {}, your appointment with Doctor {} starts in 5 minutes'.format(client.first_name, doctor.first_name)
    date_time_obj = datetime.datetime.strptime(start_time, '%Y-%m-%d %H:%M')
    
    try:
        
        createPeriodicTask(date_time_obj, doctor_id, Notification.DOCTOR, doctor_message)
        createPeriodicTask(date_time_obj, client_id, Notification.PATIENT, patient_message)   
        print("CRONTABSCHEDULE_SUCCESS: Created")    
        
    except Exception as e:
        print("CRONTABSCHEDULE_ERROR: {}".format(e))
        
    return Response({"success":True, "message":"Appointment created successfully", "error":None}, status=status.HTTP_201_CREATED)
    

@csrf_exempt
@api_view(["POST"])
@permission_classes((AllowAny,))
def bookAppointment(request):
    client_id = int(request.POST.get("client_id"))
    doctor_id = request.POST.get("doctor_id")
    about = request.POST.get("about")
    symptoms = request.POST.get("patient_symptoms")
    persistence_period = request.POST.get("perisistence_period")
    
    doctor = Doctor.objects.filter(id_number=doctor_id).first()
    client = AgapeUser.objects.filter(id_number=client_id).first()
    
    patient_symptoms = PatientSymptoms()
    patient_symptoms.symptoms = symptoms
    patient_symptoms.persistence_period=persistence_period
    patient_symptoms.patient =AgapeUser.objects.filter(id=client_id).first()
    patient_symptoms.save()
    
    appointment_request = AppointmentRequest()
    appointment_request.client=client
    appointment_request.doctor=doctor
    appointment_request.about = about
    appointment_request.symptoms = symptoms
    appointment_request.persistence_period = persistence_period
    
    appointment_request.client_first_name = client.first_name
    appointment_request.client_last_name = client.last_name
    
    if(client.profile_image):
        appointment_request.client_profile_image = client.profile_image
    
    
    appointment_request.doctor_first_name = doctor.first_name
    appointment_request.doctor_last_name = doctor.last_name
    
    if(doctor.profile_image):
        appointment_request.doctor_profile_image = doctor.profile_image
    
    appointment_request.save()
    
    return Response({"success":True, "message":"Appointment has been booked successfully", "error":None}, status=status.HTTP_201_CREATED)

def createPeriodicTask(cron_time, receiver_id, receiver_type, message):
    print("CRON_TIME: ", cron_time.minute)
    # cron_time = start_time - timedelta(minutes=5)
    cron_job, _ = CrontabSchedule.objects.get_or_create(
                            minute=cron_time.minute,
                            hour=cron_time.hour,
                            day_of_month=cron_time.day,
                            month_of_year=cron_time.month
                        )
    
    schedule, created = IntervalSchedule.objects.get_or_create(
                            every=10,
                            period=IntervalSchedule.SECONDS,
                        )
                                # id, category, message
        
    PeriodicTask.objects.create(
        crontab= cron_job,
        name='doctor_notification_{}_{}'.format(receiver_id, cron_time),
        task='agape_sockets.tasks.appointment_reminder',
        args= json.dumps((receiver_id, receiver_type, message)),
        one_off= True
    )
    

@csrf_exempt
@api_view(["POST"])
@permission_classes((AllowAny,))
def createMedicalReport(request):
    appointment = request.POST.get('appointment_id', None)
    doctor_report = request.POST.get('doctor_report', None)
    medication = request.POST.get('medication', None)
    
    try:
        medical_report = MedicalReport(
            appointment=Appointment.objects.filter(id=appointment).first(),
            medication = medication,
            doctor_report = doctor_report
        )
        
        medical_report.save()
        return Response({'success':True, 'error':None, 'message':"Report created successfully"}, status=status.HTTP_201_CREATED)
    
    except Exception as e:
        return Response({'success':False, 'error':str(e), 'message':"Sorry, we encountered an error"}, status=status.HTTP_400_BAD_REQUEST)
    
    
    
@csrf_exempt
@api_view(["GET"])
def retrieveDoctorMedicalReport(request, id):
    patient_appointments = Appointment.objects.select_related('client').filter(client__id=id)
    reports = MedicalReport.objects.select_related('appointment').filter(appointment__in=patient_appointments)
    
    
    for report in reports:
        report.doctor_name = "Dr "+report.appointment.doctor_first_name
        report.doctor_id = report.appointment.doctor.id
        report.appointment_id = report.appointment.id
        report.appointment_title =  report.appointment.title
        report.created_at = datetime.datetime.strftime(report.created_at, '%Y-%m-%d %H:%M')
        report.updated_at = datetime.datetime.strftime(report.updated_at, '%Y-%m-%d %H:%M')
    
        
    serialized_report = CustomMedicalReportSerializer(reports, many=True)
    
    print(serialized_report.data)
    
    return Response(serialized_report.data, status=status.HTTP_200_OK)
    

    
    