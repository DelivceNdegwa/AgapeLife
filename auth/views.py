import datetime
import json

from django.shortcuts import render

from .serializers import AgapeUserTokenObtainPairSerializer, RegisterUserSerializer, RegisterDoctorSerializer
from staff.models import AgapeUser, Doctor, MedicalCategory, Notification, DirectInquiryPatient, DirectInquiryMedReport

from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt

from django.core.exceptions import ObjectDoesNotExist

from rest_framework.decorators import api_view, permission_classes


from rest_framework import generics
from rest_framework.permissions import AllowAny
from rest_framework_simplejwt.views import TokenObtainPairView

from rest_framework.parsers import MultiPartParser, FormParser

from pusher_push_notifications import PushNotifications

from django.http import Http404
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

from universal import periodic_task_functions
from pusher_notifications.config import AppointmentNotifications

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
                periodic_task_functions.createBirthdayScheduler(serializer.data, Notification.PATIENT)
                return Response(serializer.data, status=status.HTTP_201_CREATED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

@csrf_exempt
@api_view(["POST"])
# @permission_classes((AllowAny,))
def doctorFormRegister(request):
    print(request.data)
    print(request.POST)
    
    # license_certificate = request.FILES['license_certificate']
    first_name = request.POST.get('first_name')
    last_name = request.POST.get('last_name')
    id_number = request.POST.get('id_number')
    username = first_name+"_"+id_number
    email = request.POST.get('email')
    phone_number = request.POST.get('phone_number')
    password = request.POST.get('password')
    hospital = request.POST.get('hospital')
    speciality = request.POST.get('speciality')
    category_id = int(request.POST.get('category'))
    
    experience_years = request.POST.get('experience_years')
    gender = int(request.POST.get('gender'))
    age = int(request.POST.get('age'))
    
    date_of_birth = request.POST.get('date_of_birth')
    dob_obj = datetime.datetime.strptime(date_of_birth, '%Y-%m-%d').date()
    
    # profile_image = request.FILES['profile_image']
    
    errors = validate_fields(username, email, id_number, phone_number)
    
    if errors:
        print(errors)
        return Response(errors, status=status.HTTP_400_BAD_REQUEST)
    
    else:
        doctor = Doctor()
        # doctor.license_certificate = license_certificate
        doctor.email = email
        doctor.username = username
        # doctor.profile_image = profile_image
        doctor.set_password(password)
        doctor.id_number = id_number
        doctor.phone_number = phone_number
        doctor.hospital = hospital
        doctor.speciality = speciality
        doctor.first_name = first_name
        doctor.last_name = last_name
        
        doctor.gender = gender
        doctor.age = age
        # doctor.experience_years = experience_years
        doctor.date_of_birth = dob_obj
        doctor.category = MedicalCategory.objects.filter(id=category_id).first()
        doctor.save()
        
        return Response({'success':'Registration successful'}, status=status.HTTP_201_CREATED)


def validate_fields(username, email, id_number, phone_number):
    username = Doctor.objects.filter(username=username).first()
    email = Doctor.objects.filter(email=email).first()
    id_number = Doctor.objects.filter(id_number=id_number).first()
    phone_number = Doctor.objects.filter(phone_number=phone_number).first()
    
    errors = {}
    
    if username:
        errors['username'] = 'This username is already taken'
    elif email:
        errors['email'] = 'This email is already taken'
    elif id_number:
        errors['id_number'] = 'This ID Number is already taken'
    elif phone_number:
        errors['phone_number'] = 'This phone number is already taken'
        
    else:
        pass
    
    
    return errors    
    

@csrf_exempt
@api_view(["POST"])
@permission_classes((AllowAny,))
def uploadDoctorFiles(request, id):
    print("DOCTOR_ID: ", id)
    print("FILE_POST: ", request.POST)
    print("fILE_DATA: ", request.data)    # path('reg-doc/', testMultipartMap),
    
    license_certificate = request.FILES['license_certificate']
    profile_image = request.FILES['profile_image']
    
    print("License:", license_certificate)
    print("Profile: ", profile_image)
    
    doctor = Doctor.objects.filter(id_number=id).first()
    
    doctor.license_certificate = license_certificate
    doctor.profile_image = profile_image
    doctor.save()
    
    
    return Response({'success':'Uploaded files for doctor {}'.format(doctor.username)}, status=status.HTTP_201_CREATED)


@csrf_exempt
@api_view(["POST"])
def registerInstantPatient(request):
    registration = request.POST
    print(f"PATIENT-DETAILS: {request.data}, {registration}")    
    first_name = registration.get("first_name")
    last_name = registration.get("last_name")
    national_id = registration.get("national_id")
    phone = registration.get("phone")
    age = registration.get("age")
    doctor_id = registration.get("doctor_id")
    
    patient_search = DirectInquiryPatient.objects.filter(
                                                    firstname = first_name,
                                                    lastname = last_name,
                                                    national_id = int(national_id),
                                                    age = int(age),
                                                    phone = int(phone)
                                                ).first()
    
    
    if not patient_search:
        
        doctor = Doctor.objects.filter(id=doctor_id).first()
        
        patient = DirectInquiryPatient()
        patient.firstname = first_name
        patient.lastname = last_name
        patient.national_id = national_id
        patient.age = age
        patient.phone = phone
        patient.has_accepted_terms = True
        patient.doctor = doctor
        patient.save()
        
        message = "Patient created successfully"
        patient_id = patient.id
        
        return Response({'success': data}, status=status.HTTP_201_CREATED)
        
    else:
        message = "Patient was previously added into the system"
        patient_id = patient_search.id

        
        data = {
            "message": message,
            "patient_id": patient_id
        }
    
        return Response({'success': data}, status=status.HTTP_200_OK)


@csrf_exempt
@api_view(["GET"])
@permission_classes((AllowAny,))
def getPusherTokenForAuth(request, id, user_type):
    DOCTOR = 1
    PATIENT = 2
    
    if user_type == DOCTOR:
        user = Doctor.objects.filter(id=id).first()
        
    elif user_type == PATIENT:
        user = AgapeUser.objects.filter(id=id).first()
    
    else:
        return Response({"user_type_error": "User type was not recognized"}, status=status.HTTP_400_BAD_REQUEST)
        
    if user is not None:
        appointment_notification = AppointmentNotifications()
        
        beams_client = PushNotifications(
            appointment_notification.get_instance_id(),
            appointment_notification.get_key()
        )
        
        beams_token = beams_client.generate_token(user.id_number)
        return Response({"beams_token": beams_token}, status=status.HTTP_200_OK)
        
        
