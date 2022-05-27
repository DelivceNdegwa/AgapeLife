from staff.models import AppointmentRequest, Appointment, Doctor, LoggedInDoctor
from django.db.models import Q


def get_total_appointments_requests(id):
    return AppointmentRequest.objects.select_related('doctor').filter(
        doctor__id=id, status=AppointmentRequest.PENDING
    )

def get_total_appointments(id):
    return Appointment.objects.select_related('client').filter(client__id=id).exclude(
        Q(status= Appointment.CANCELLED) | Q(status= Appointment.COMPLETE)
    )