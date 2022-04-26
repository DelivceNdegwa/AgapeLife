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
