from django.http import HttpResponse
from django.shortcuts import render


from .models import *
# from .tasks import test_func

def sockets(request):
    return render(request, 'sockets.html')

def doctor_room(request, id):
    
    context = {
        'doctor_id': id,
        }
    
    return render(request, 'doctor_appointments.html', context)

def patient_room(request, id):
    context = {
        'patient_id': id
    }
    
    return render(request, 'patient_appointments.html', context)
