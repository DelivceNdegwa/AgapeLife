from rest_framework import serializers
from staff.models import *


class MedicalCategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = MedicalCategory
        fields = '__all__'
        

class AgapeUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = AgapeUser
        fields = '__all__'
        

class DoctorSerializer(serializers.ModelSerializer):
    class Meta:
        model = Doctor
        fields = ('id', 'is_verified', 'profile_image', 'hospital', 'speciality', 'category', 'self_description', 'experience_years', 'is_available', 'first_name', 'last_name', 'id_number','phone_number', 'phone_number', 'password')
        
        
class MedicalTipsSerializer(serializers.ModelSerializer):
    class Meta:
        model = MedicalTips
        fields = '__all__'
        
        
class UserFeedbackSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserFeedback
        fields = '__all__'
        
        
class DoctorCategoryPivotSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = DoctorCategoryPivot
        fields = '__all__'
        
        
class AppointmentSerializer(serializers.ModelSerializer):
    class Meta:
        model = Appointment
        fields = '__all__'


class AppointmentRequestSerializer(serializers.ModelSerializer):
    class Meta:
        model = AppointmentRequest
        fields   = '__all__'
        
        
class PatientSymptomsSerializer(serializers.ModelSerializer):
    class Meta:
        model = PatientSymptoms
        fields = '__all__'
        

class MedicalReportSerializer(serializers.ModelSerializer):
    class Meta:
        model = MedicalReport
        fields = '__all__'
        
        
class DirectInquiryMedReportSerializer(serializers.ModelSerializer):
    class Meta:
        model = DirectInquiryMedReport
        fields = '__all__'

class CustomMedicalReportSerializer(serializers.Serializer):
    doctor_name = serializers.CharField(max_length=100)
    doctor_id = serializers.IntegerField()
    medication = serializers.CharField(max_length=300)
    doctor_report = serializers.CharField(max_length=500)
    appointment_id = serializers.IntegerField()
    appointment_title = serializers.CharField(max_length=150)
    created_at = serializers.CharField(max_length=100)
    updated_at = serializers.CharField(max_length=100)
            
        
