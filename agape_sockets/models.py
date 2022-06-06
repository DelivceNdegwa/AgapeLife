from django.db import models
from django.contrib.auth.models import User

from django.db.models.signals import post_save

from django.dispatch import receiver
from channels.layers import get_channel_layer

from staff.models import *

from asgiref.sync import async_to_sync

import json

class TestSocketMonitor(User):
    class Meta:
        verbose_name = 'Test User Monitor'
    def __str__(self):
        return self.username
class TestSocketModel(models.Model):
    READ = 1
    UNREAD = 2
    
    STATUS_CHOICES = (
        (READ, "READ"),
        (UNREAD, "UNREAD")
    )
    
    name = models.CharField(max_length=30)
    status = models.IntegerField(choices=STATUS_CHOICES, default=UNREAD)
    user = models.ForeignKey(TestSocketMonitor, on_delete=models.CASCADE, null=True, blank=True)
    
    def __str__(self):
        return self.name

    def getStatus(self):
        if self.status == self.READ:
            return "READ"
        return "UNREAD" 
    

class AgapeUser1(User):
    phone_number = models.IntegerField()
    id_number = models.IntegerField()
    
    class Meta:
        verbose_name='Agape User'
    
    def __str__(self):
        return str(self.id_number)+" "+self.email


class Doctor1(User):
    is_verified = models.BooleanField(default=False)
    license_certificate = models.FileField(upload_to='license/', null=True, blank=True)
    profile_image = models.ImageField(upload_to='media/', null=True, blank=True)
    hospital = models.CharField(max_length=200)
    experience_years = models.IntegerField(null=True, blank=True)
    speciality = models.CharField(max_length=100)
    # category = models.ForeignKey(MedicalCategory, null=True, blank=True, on_delete=models.SET_NULL)
    phone_number = models.IntegerField(null=True)
    id_number = models.IntegerField(null=True)
    self_description = models.TextField(null=True, blank=True)
    is_available = models.BooleanField(default=False)
    # created_at = models.DateTimeField(auto_now=True)
    # updated_at = models.DateTimeField(auto_now_add=True)
    
    @property
    def get_status(self):
        if self.is_verified:
            return "Verified"
        return "Pending"
    
    class Meta:
        verbose_name = 'Agape Doctor'
    
    # def __str__(self):
    #     return "Dr "+self.first_name+" "+self.last_name
    def __str__(self):
        return str(self.id)

class PatientSymptoms1(models.Model):
    symptoms = models.TextField()
    patient = models.ForeignKey(AgapeUser1, null=True,  on_delete=models.SET_NULL)
    viewed_by = models.ForeignKey(Doctor1, null=True, blank=True,  on_delete=models.SET_NULL)
    
    class Meta:
        verbose_name_plural = 'Patient Symptoms'
    
    def __str__(self):
        return str(self.patient)
    
    
   
    
class AppointmentRequest1(models.Model):
    APPROVED = 1
    DISAPPROVED = 2
    PENDING = 3
    
    STATUS = (
        (PENDING, "Pending"),
        (APPROVED, "Approved"),
        (DISAPPROVED, "Disapproved")
    )
    client = models.ForeignKey(AgapeUser1, on_delete=models.CASCADE)
    doctor = models.ForeignKey(Doctor1, models.CASCADE)
    about = models.CharField(max_length=50)
    symptoms = models.ForeignKey(PatientSymptoms1, on_delete=models.PROTECT, null=True, blank=True)
    status = models.IntegerField(choices=STATUS, default=PENDING)
    read = models.BooleanField(default=False)
    
    # def __init__(self, *args, **kwargs):
    #     super(AppointmentRequest, self).__init__(*args, **kwargs)
    #     self._original_status= self.status
    
    
    def __str__(self):
        if self.read == True:
            return 'Read'
        else:
            return 'Unread'
    
    def get_status(self):
        status_dictionary = {
            self.APPROVED: "APPROVED",
            self.DISAPPROVED: "DISAPPROVED",
            self.PENDING: "PENDING"
        }
        
        return status_dictionary[self.status]    
       