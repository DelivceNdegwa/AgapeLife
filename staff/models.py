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
    profile_image = models.ImageField(upload_to='media/', null=True, blank=True)
    
    def __get_first_name(self):
        return self.first_name
    
    def __get_last_name(self):
        return self.last_name
    
    get_first_name = property(__get_first_name)
    get_last_name = property(__get_last_name)
    
    class Meta:
        verbose_name='Agape User'
    
    # def __str__(self):
    #     return str(self.id_number)+" "+self.email
    def __str__(self):
        return str(self.id_number)


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
    
    def __get_category_id(self):
        return self.category.id
    
    def __get_first_name(self):
        return self.first_name
    
    def __get_last_name(self):
        return self.last_name
    
    get_first_name = property(__get_first_name)
    get_last_name = property(__get_last_name)
    category_id = property(__get_category_id)
    
    class Meta:
        verbose_name = 'Agape Doctor'
        indexes = [
            models.Index(fields=['is_verified', 'is_available']),
            models.Index(fields=['is_verified']),
            models.Index(fields=['is_available'])
        ]
    
    # def __str__(self):
    #     return "Dr "+self.first_name+" "+self.last_name
    def __str__(self):
        return str(self.id_number)

class PatientSymptoms(models.Model):
    symptoms = models.TextField()
    persistence_period = models.CharField(max_length=30, null=True)
    patient = models.ForeignKey(AgapeUser, null=True,  on_delete=models.SET_NULL)
    viewed_by = models.ForeignKey(Doctor, null=True, blank=True,  on_delete=models.SET_NULL)
    created_at = models.DateTimeField(auto_now=True, null=True)
    updated_at = models.DateTimeField(auto_now_add=True, null=True)
    
    class Meta:
        verbose_name_plural = 'Patient Symptoms'
    
    def __str__(self):
        return str(self.patient)
    

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
    symptoms = models.TextField(null=True, blank=True)
    persistence_period = models.CharField(max_length=30, null=True, blank=True)
    status = models.IntegerField(choices=STATUS, default=PENDING)
    read = models.BooleanField(default=False)
    
    def __get_client_first_name(self):
        return self.client.first_name
    
    def __get_client_last_name(self):
        return self.client.last_name
    
    def __get_client_profile_image(self):
        return self.client.profile_image
    
    def __get_client_id(self):
        return self.client.id
    
    def __get_doctor_id(self):
        return self.doctor.id
    
    def __get_doctor_first_name(self):
        return self.doctor.get_first_name
    
    def __get_doctor_last_name(self):
        return self.doctor.get_last_name
    
    def __get_doctor_profile_image(self):
        return self.doctor.profile_image
    
    client_first_name = property(__get_client_first_name)
    client_last_name = property(__get_client_last_name)
    client_profile_image = property(__get_client_profile_image)
        
    doctor_first_name = property(__get_doctor_first_name)
    doctor_last_name = property(__get_doctor_last_name)
    doctor_profile_image = property(__get_doctor_profile_image)
    
    doctor_id = property(__get_doctor_id)
    client_id = property(__get_client_id)
    
    def __str__(self):
        if self.read == True:
            return 'Read'
        else:
            return 'Unread: '+self.about
    
    def get_status(self):
        status_dictionary = {
            self.APPROVED: "APPROVED",
            self.DISAPPROVED: "DISAPPROVED",
            self.PENDING: "PENDING"
        }
        
        return status_dictionary[self.status]

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
    
    def __get_doctor_id(self):
        return self.doctor.id

    def __get_client_id(self):
        return self.client.id
    
    def __get_client_first_name(self):
        return self.client.first_name
    
    def __get_client_last_name(self):
        return self.client.last_name
    
    def __get_client_profile_image(self):
        return self.client.profile_image
    
    def __get_doctor_first_name(self):
        return self.doctor.first_name
    
    def __get_doctor_last_name(self):
        return self.doctor.last_name
    
    def __get_doctor_profile_image(self):
        return self.doctor.profile_image
    
    client_id = property(__get_client_id)
    doctor_id = property(__get_doctor_id)
    
    client_first_name = property(__get_client_first_name)
    client_last_name = property(__get_client_last_name)
    client_profile_image = property(__get_client_profile_image)
    
    doctor_first_name = property(__get_doctor_first_name)
    doctor_last_name = property(__get_doctor_last_name)
    doctor_profile_image = property(__get_doctor_profile_image)
        
    def __str__(self):
        return str(self.about)
    
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
    

class Notification(models.Model):
    DOCTOR = 1
    PATIENT = 2
    
    RECIPIENT_TYPE=(
        (DOCTOR, "DOCTOR"),
        (PATIENT, "PATIENT")
    )
    
    recipient_category = models.IntegerField(choices=RECIPIENT_TYPE)
    recipient_id = models.IntegerField()
    message = models.CharField(max_length=300)
    
    def __str__(self):
        return str(self.recipient_id)
    

class MedicalReport(models.Model):
    doctor = models.ForeignKey(Doctor, on_delete=models.PROTECT)
    patient = models.ForeignKey(AgapeUser, on_delete=models.PROTECT)
    medication = models.TextField(null=True)
    doctor_report = models.TextField()
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return "patient_{}_{}_report".format(self.patient.first_name, self.patient.id)

            