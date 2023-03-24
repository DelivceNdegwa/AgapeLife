from django.db import models
from django.contrib.auth.models import User

from django.conf import settings


MALE=1
FEMALE=2

GENDER_CHOICES = (
    (MALE, "Male"),
    (FEMALE, "Female")
)

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
    gender = models.IntegerField(choices=GENDER_CHOICES, null=True, blank=True)
    age = models.IntegerField(null=True, blank=True)
    
    date_of_birth = models.DateField(null=True, blank=True)
    
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
        return str(self.id)


class Doctor(User):
    is_verified = models.BooleanField(default=False)
    is_available = models.BooleanField(default=False)
    
    mobile_fingerprint = models.TextField(null=True)
    
    license_certificate = models.FileField(upload_to='license/', null=True, blank=True)
    profile_image = models.ImageField(upload_to='media/', null=True, blank=True)
    front_id_part = models.ImageField(upload_to='front_id_part/', null=True, blank=True)
    back_id_part = models.ImageField(upload_to='back_id_part/', null=True, blank=True)
    
    hospital = models.CharField(max_length=200)
    speciality = models.CharField(max_length=100)
    category = models.ForeignKey(MedicalCategory, null=True, blank=True, on_delete=models.SET_NULL)
    
    self_description = models.TextField(default="No description provided.", blank=True)
    
    id_number = models.IntegerField(null=True)
    gender = models.IntegerField(choices=GENDER_CHOICES, null=True, blank=True)
    age = models.IntegerField(null=True, blank=True)
    phone_number = models.IntegerField(null=True)
    experience_years = models.IntegerField(null=True, blank=True)
    
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    date_of_birth = models.DateField(null=True, blank=True)


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
    get_category_id = property(__get_category_id)
    
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

    created_at = models.DateTimeField(auto_now=True, blank=True)
    updated_at = models.DateTimeField(auto_now_add=True, null=True)
    
    client_first_name = models.CharField(max_length=30, null=True)
    client_last_name = models.CharField(max_length=30, null=True)
    client_profile_image = models.ImageField(upload_to="media/", null=True)
    
    doctor_first_name = models.CharField(max_length=30, null=True)
    doctor_last_name = models.CharField(max_length=30, null=True)
    doctor_profile_image = models.ImageField(upload_to="media/", null=True)

    
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
    
    PUBLISHER = 1
    SUBSCRIBER = 2
    
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
    
    appointment_token = models.CharField(max_length=300, null=True, blank=True)
    
    client_first_name = models.CharField(max_length=30, null=True)
    client_last_name = models.CharField(max_length=30, null=True)
    client_profile_image = models.ImageField(upload_to="media/", null=True)
    
    doctor_first_name = models.CharField(max_length=30, null=True)
    doctor_last_name = models.CharField(max_length=30, null=True)
    doctor_profile_image = models.ImageField(upload_to="media/", null=True)

        
    def __str__(self):
        return str(self.id)
    
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
    
    APPOINTMENT = 1
    AGAPE_INFO = 2
    BIRTHDAY_WISH = 3
    
    RECIPIENT_TYPE=(
        (DOCTOR, "DOCTOR"),
        (PATIENT, "PATIENT")
    )
    
    MESSAGE_TYPE = (
        (APPOINTMENT, "APPOINTMENT"),
        (AGAPE_INFO, "AGAPE INFO"),
        (BIRTHDAY_WISH, "BIRTHDAY WISH")
    )
    
    
    recipient_category = models.IntegerField(choices=RECIPIENT_TYPE)
    recipient_id = models.IntegerField()
    notification_type = models.IntegerField(choices=MESSAGE_TYPE, default=APPOINTMENT)
    message = models.CharField(max_length=300)
    created_at = models.DateTimeField(auto_now=True, null=True)
    updated_at = models.DateField(auto_now_add=True, null=True)
    
    def __str__(self):
        return str(self.notification_type)
    

class MedicalReport(models.Model):
    appointment = models.ForeignKey(Appointment, on_delete=models.PROTECT, null=True, blank=False)
    medication = models.TextField(null=True)
    doctor_report = models.TextField()
    created_at = models.DateTimeField(auto_now=True)
    updated_at = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return "patient_{}_{}_report".format(self.appointment.client.first_name, self.appointment.client.id)
    
    

class DirectInquiryPatient(models.Model):
    firstname = models.CharField(max_length=100)
    lastname = models.CharField(max_length=100)
    id = models.IntegerField()
    phone = models.IntegerField()
    age = models.IntegerField()
    has_accepted_terms = models.BooleanField()
    
    doctor = models.ForeignKey(Doctor, on_delete=models.PROTECT)
    
    def __str__(self):
        return f"{self.firstname} {self.lastname} {self.id}"
    
    
    
class DirectInquiryMedReport(models.Model):
    symptoms = models.TextField()
    suspected_illness = models.CharField(max_length=200)
    prescription = models.TextField(null=True)
    recommendation = models.TextField(null=True)
    patient = models.ForeignKey(DirectInquiryPatient, on_delete=models.PROTECT)
    
    def __str__(self):
        return f"{self.patient} suspected for {self.suspected_illness}"
    
    



            