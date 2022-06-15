from staff.models import *
from django.db.models import Q

online_doctors_group = 'socket_online_doctors'

def get_online_doctors():
    return Doctor.objects.filter(is_verified=True, is_available=True)

def get_doctor_appointment_requests(id):
    return AppointmentRequest.objects.select_related('doctor').filter(
                                    doctor__id_number=id, status=AppointmentRequest.PENDING
                                )
    
def get_doctor_upcoming_appointment(id):
    return Appointment.objects.select_related('doctor').filter(
                                doctor_id_number=id, status=Appointment.PENDING
                            )
    
def patient_appointments(id):
    return Appointment.objects.select_related('client').filter(
                                 client__id_number=id
                            )

def get_single_doctor(id):
    return Doctor.objects.filter(id=id).first()
    
