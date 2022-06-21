from staff.models import *
from django.db.models import Q

online_doctors_group = 'socket_online_doctors'

def get_online_doctors():
    return Doctor.objects.filter(is_verified=True, is_available=True).order_by('-updated_at')

def get_doctor_appointment_requests(id):
    return AppointmentRequest.objects.select_related('client').filter(
                                    doctor__id_number=id, status=AppointmentRequest.PENDING
                                )
    
def get_doctor_upcoming_appointment(id):
    
    return Appointment.objects.select_related('client').filter(
                                doctor__id_number=id, status=Appointment.PENDING
                            )

    
def patient_appointments(id):
    patients= Appointment.objects.select_related('doctor').filter(
                                 client__id_number=id
                            ).order_by('-start_time')
   
    return patients

def patient_appointment_requests(id):
    return AppointmentRequest.objects.select_related('doctor').filter(
                                client__id_number=id
                            ).order_by('-created_at')

def get_single_doctor(id):
    return Doctor.objects.filter(id=id).first()
    
