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
        return self.name



class AgapeUser(User):
    phone_number = models.IntegerField()
    id_number = models.IntegerField()
    
    class Meta:
        verbose_name='Agape User'
    
    def __str__(self):
        return str(self.id_number)+" "+self.email
    

class Doctor(User):
    is_verified = models.BooleanField(default=False)
    license_certificate = models.FileField(upload_to='license/', null=True, blank=True)
    profile_image = models.ImageField(upload_to='media/', null=True, blank=True)
    hospital = models.CharField(max_length=200)
    experience_years = models.IntegerField(null=True, blank=True)
    speciality = models.CharField(max_length=100)
    category = models.ForeignKey(MedicalCategory, null=True, blank=True, on_delete=models.SET_NULL)
    phone_number = models.IntegerField(null=True)
    id_number = models.IntegerField(null=True)
    self_description = models.TextField(null=True, blank=True)
    is_available = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    @property
    def get_status(self):
        if self.is_verified:
            return "Verified"
        return "Pending"
    
    class Meta:
        verbose_name = 'Agape Doctor'
    
    def __str__(self):
        return "Dr "+self.first_name+" "+self.last_name
    

class AppointmentRequest(models.Model):
    APPROVED = 1
    DISAPPROVED = 2
    PENDING = 3
    
    STATUS = (
        (PENDING, "Pending"),
        (APPROVED, "Approved"),
        (DISAPPROVED, "Disapproved")
    )
    client = models.ForeignKey(AgapeUser, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, models.CASCADE)
    about = models.CharField(max_length=50)
    symptoms = models.TextField()
    status = models.IntegerField(choices=STATUS, default=PENDING)
    read = models.BooleanField(default=False)
    
    def __str__(self):
        if self.read == True:
            return 'Read'
        else:
            return 'Unread'

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
    title = models.CharField(max_length=50, default="My appointment")
    about = models.CharField(max_length=200, null=True, blank=True)
    start_time = models.DateTimeField()
    end_time = models.DateTimeField()
    doctor = models.ForeignKey(Doctor, null=True, blank=True, on_delete=models.SET_NULL)
    client = models.ForeignKey(AgapeUser, null=True,  on_delete=models.SET_NULL)
    status = models.IntegerField(choices=APPOINTMENT_STATUS, default=PENDING)
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return str(self.about)
    
    
class PatientSymptoms(models.Model):
    symptoms = models.TextField()
    patient = models.ForeignKey(AgapeUser, null=True,  on_delete=models.SET_NULL)
    viewed_by = models.ForeignKey(Doctor, null=True, blank=True,  on_delete=models.SET_NULL)
    
    def __str__(self):
        return str(self.patient.id_number)
    
    
class DoctorPrescription(models.Model):
    medicine = models.TextField(null=True, blank=True)
    recommendation = models.TextField()
    prescription_to = models.ForeignKey(AgapeUser, on_delete=models.CASCADE)
    prescribed_by = models.ForeignKey(Doctor, null=True, blank=True, on_delete=models.CASCADE)
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
    status = models.IntegerField(choices=FEEDBACK_STATUS, default=UNREAD)
    category = models.IntegerField(choices=FEEDBACK_CATEGORY, default=APP_RELATED)
    
    def __str__(self):
        return str(self.author.author.id_number)

   
    
class DoctorCategoryPivot(models.Model):
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE, null=True, blank=True)
    category = models.ForeignKey(MedicalCategory, on_delete=models.CASCADE)
    
    def __str__(self):
        return str(self.doctor.user.id_number)+" "+self.category.name
    
    
class EditorDoctorPivot(models.Model):
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE, null=True, blank=True)
    editor = models.ForeignKey(AgapeUser, on_delete=models.CASCADE)
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=False)
    
    def __str__(self):
        return str(self.doctor)

    
class LoggedInDoctor(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, related_name='logged_in_doctor', on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor, on_delete=models.CASCADE, null=True, blank=True)
    
    def __str__(self):
        return self.user.username
    

