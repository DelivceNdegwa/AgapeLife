from django.shortcuts import render

from .serializers import AgapeUserTokenObtainPairSerializer, RegisterUserSerializer, RegisterDoctorSerializer
from staff.models import AgapeUser, Doctor, MedicalCategory

from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt

from rest_framework.decorators import api_view, permission_classes


from rest_framework import generics
from rest_framework.permissions import AllowAny
from rest_framework_simplejwt.views import TokenObtainPairView

from rest_framework.parsers import MultiPartParser, FormParser

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
    

@csrf_exempt
@api_view(["POST"])
@permission_classes((AllowAny,))
def doctorRegister(request):
    # print(request.data)
    
    license_certificate = request.FILES['license_certificate']
    first_name = request.POST.get('first_name')
    last_name = request.POST.get('last_name')
    username = request.POST.get('first_name')
    email = request.POST.get('email')
    id_number = request.POST.get('id_number')
    phone_number = request.POST.get('phone_number')
    password = request.POST.get('password')
    hospital = request.POST.get('hospital')
    speciality = request.POST.get('speciality')
    category_id = int(request.POST.get('category'))
    
    errors = validate_fields(username, email, id_number, phone_number)
    
    if errors:
        print(errors)
        return Response(errors, status=status.HTTP_400_BAD_REQUEST)
    
    else:
        doctor = Doctor()
        doctor.license_certificate = license_certificate
        doctor.email = email
        doctor.username = username
        doctor.set_password(password)
        doctor.id_number = id_number
        doctor.phone_number = phone_number
        doctor.hospital = hospital
        doctor.speciality = speciality
        doctor.first_name = first_name
        doctor.last_name = last_name
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
    


