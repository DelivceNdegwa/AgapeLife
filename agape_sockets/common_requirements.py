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
    
    appointments =  Appointment.objects.select_related('client').filter(
                                doctor__id_number=id, status=Appointment.PENDING
                            )
    
    # for appointment in appointments:
    #     appointment.client_name = appointment.client.first_name + " "+appointment.client.last_name
    #     # print("DOCTOR_UPCOMING_APPOINTMENT: {}, {}".format(appointment.doctor_fname, appointment.doctor_lname))
    
    return appointments
    
def patient_appointments(id):
    return Appointment.objects.select_related('client').filter(
                                 client__id_number=id
                            )

def get_single_doctor(id):
    return Doctor.objects.filter(id=id).first()
    
