from wsgiref import validate
from rest_framework import serializers
from staff.models import AgapeUser, Doctor, MedicalCategory
from rest_framework.validators import UniqueValidator
from django.contrib.auth.password_validation import validate_password

from rest_framework_simplejwt.serializers import TokenObtainPairSerializer


class AgapeUserTokenObtainPairSerializer(TokenObtainPairSerializer):

    @classmethod
    def get_token(cls, user):
        token = super(AgapeUserTokenObtainPairSerializer, cls).get_token(user)

        # Add custom claims
        token['username'] = user.username
        return token
    

class RegisterUserSerializer(serializers.ModelSerializer):
    email = serializers.EmailField(
            required=True,
            validators=[UniqueValidator(queryset=AgapeUser.objects.all())]
            )
    
    id_number = serializers.CharField(
        required=True,
        validators=[UniqueValidator(queryset=AgapeUser.objects.all())]
    )
    
    phone_number = serializers.CharField(
        required=True,
        validators=[UniqueValidator(queryset=AgapeUser.objects.all())]
    )
    
    username = serializers.CharField(
        required=False
    )

    password = serializers.CharField(write_only=True, required=True, validators=[validate_password])
    password2 = serializers.CharField(write_only=True, required=True)

    class Meta:
        model = AgapeUser
        fields = ('username', 'phone_number', 'id_number', 'password', 'password2', 'email', 'first_name', 'last_name')
        extra_kwargs = {
            'first_name': {'required': True},
            'last_name': {'required': True},
        }

    def validate(self, attrs):
        if attrs['password'] != attrs['password2']:
            raise serializers.ValidationError({"password": "Password fields didn't match."})

        return attrs

    def create(self, validated_data):
        user = AgapeUser.objects.create(
            username=validated_data['username'],
            email=validated_data['email'],
            first_name=validated_data['first_name'],
            last_name=validated_data['last_name'],
            phone_number=validated_data['phone_number'],
            id_number=validated_data['id_number']
        )

        
        user.set_password(validated_data['password'])
        user.save()

        return user
    

class RegisterDoctorSerializer(serializers.Serializer):
    license_certificate = serializers.FileField(required=True)
    profile_image = serializers.ImageField(required=True)
    hospital = serializers.CharField(required=True)
    speciality = serializers.CharField(required=True)
    category = serializers.CharField(required=True)
    # user = serializers.CharField(required=True)
    
    first_name = serializers.CharField(required=True)
    last_name = serializers.CharField(required=True)
    
    self_description = serializers.CharField(required=True)
    experience_years = serializers.CharField(required=True)
    
    email = serializers.EmailField(
            required=True,
            validators=[UniqueValidator(queryset=AgapeUser.objects.all())]
            )
    
    id_number = serializers.CharField(
        required=True,
        validators=[UniqueValidator(queryset=AgapeUser.objects.all())]
    )
    
    phone_number = serializers.CharField(
        required=True,
        validators=[UniqueValidator(queryset=AgapeUser.objects.all())]
    )
    
    username = serializers.CharField(
        required=False
    )

    password = serializers.CharField(write_only=True, required=True, validators=[validate_password])
    password2 = serializers.CharField(write_only=True, required=True)

    
    
    class Meta:
        model=Doctor
        fields = ('license_certificate', 'profile_image', 'hospital', 'speciality',  'category', 'username', 'email', 'first_name', 'last_name', 'password', 'password2', 'id_number', 'phone_number')
    
    def validate(self, attrs):
        if attrs['password'] != attrs['password2']:
            raise serializers.ValidationError({"password": "Password fields didn't match."})

        return attrs

    def create(self, validated_data):
        id_category = validated_data['category']

        category_instance = MedicalCategory.objects.filter(id=id_category).first()
        
        doctor = Doctor.objects.create(
            license_certificate = validated_data['license_certificate'],
            profile_image = validated_data['profile_image'],
            hospital = validated_data['hospital'],
            speciality = validated_data['speciality'],
            category = category_instance,
            username=validated_data['username'],
            email=validated_data['email'],
            first_name=validated_data['first_name'],
            last_name=validated_data['last_name'],
            phone_number=validated_data['phone_number'],
            id_number=validated_data['id_number']
            
        )
        doctor.set_password(validated_data['password'])
        doctor.save()
        
        return doctor