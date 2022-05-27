from django.shortcuts import render
from .models import *


def sockets(request):
    return render(request, 'sockets.html')

def room(request, room_name):
    
    context = {
        'room_name': room_name,
        }
    
    return render(request, 'doctor_appointments.html', context)