from distutils.command.upload import upload
from pickletools import UP_TO_NEWLINE
from random import choices
from statistics import mode
from xml.etree.ElementTree import QName
from django.db import models
from django.contrib.auth.models import User

from django.conf import settings



class MedicalCategory(models.Model):
    name = models.CharField(max_length=200)
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    class Meta:
        verbose_name_plural = 'Medical Categories'

    def __str__(self):
        self.name



class AgapeUser(User):
    phone_number = models.IntegerField()
    id_number = models.IntegerField()
    
    def __str__(self):
        return str(self.id_number)+" "+self.email
    

class Doctor(models.Model):
    is_verified = models.BooleanField(default=False)
    license_certificate = models.FileField(upload_to='license/')
    profile_image = models.ImageField(null=True, blank=True)
    hospital = models.CharField(max_length=200)
    speciality = models.CharField(max_length=100)
    category = models.ForeignKey(MedicalCategory, null=True, blank=True, on_delete=models.SET_NULL)
    user = models.ForeignKey(AgapeUser, on_delete=models.CASCADE, null=False, blank=False)
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return str(self.user.id_number)
    
    
class Appointment(models.Model):
    COMPLETE = 1
    ONGOING = 2
    CANCELLED = 3
    PENDING = 4
    
    APPOINTMENT_STATUS=(
        (PENDING, "Pending"),
        (ONGOING, "Ongoing"),
        (COMPLETE, "Complete"),
        (CANCELLED, "Cancelled")
    )
    
    about = models.CharField(max_length=200)
    start_time = models.DateTimeField()
    end_time = models.DateTimeField()
    doctor = models.ForeignKey(Doctor, null=True,  on_delete=models.SET_NULL)
    client = models.ForeignKey(AgapeUser, null=True,  on_delete=models.SET_NULL)
    status = models.CharField(choices=APPOINTMENT_STATUS, max_length=30, default=PENDING)
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return str(self.about)
    
    
class PatientSymptoms(models.Model):
    symptoms = models.TextField()
    patient = models.ForeignKey(AgapeUser, null=True,  on_delete=models.SET_NULL)
    viewed_by = models.ForeignKey(Doctor, null=True,  on_delete=models.SET_NULL)
    
    def __str__(self):
        return str(self.patient.id_number)
    
    
class DoctorPrescriptions(models.Model):
    medicine = models.TextField(null=True, blank=True)
    recommendation = models.TextField()
    prescription_to = models.ForeignKey(AgapeUser, on_delete=models.CASCADE)
    prescribed_by = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    created_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return str(self.prescription_to.id_number)
    

class MedicalTips(models.Model):
    author = models.ForeignKey(User, on_delete=models.CASCADE)
    sourced_from = models.TextField(null=True, blank=True)
    title = models.CharField(max_length=100)
    slide_image = models.ImageField(upload_to="media/")    
    description = models.TextField()
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    class Meta():
        verbose_name_plural = 'Medical Tips'
        
    def __str__(self):
        return str(self.created_at)
    
    
class UserFeedback(models.Model):
    DOCTOR_RELATED = 1
    APP_RELATED = 2
    
    READ = 1
    UNREAD = 1
    
    FEEDBACK_CATEGORY = (
        (DOCTOR_RELATED, "Doctor Related"),
        (APP_RELATED, "App Related")
    )
    
    FEEDBACK_STATUS = (
        (READ, "Read"),
        (UNREAD, "Unread")
    )
    
    author = models.ForeignKey(AgapeUser, on_delete=models.CASCADE)
    message = models.TextField()
    status = models.CharField(choices=FEEDBACK_STATUS, default=UNREAD, max_length=30)
    category = models.CharField(choices=FEEDBACK_CATEGORY, default=APP_RELATED, max_length=30)
    
    def __str__(self):
        return str(self.author.author.id_number)

   
    
class DoctorCategoryPivot(models.Model):
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    category = models.ForeignKey(MedicalCategory, on_delete=models.CASCADE)
    
    def __str__(self):
        return str(self.doctor.user.id_number)+" "+self.category.name
    
    
class EditorDoctorPivot(models.Model):
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    editor = models.ForeignKey(AgapeUser, on_delete=models.CASCADE)
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=False)
    
    def __str__(self):
        return str(self.doctor)

    
class LoggedInDoctor(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, related_name='logged_in_doctor', on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE)
    
    def __str__(self):
        return self.user.username
    

