from django.shortcuts import render
from .models import *


def sockets(request):
    return render(request, 'sockets.html')

def room(request, id):
    
    context = {
        'room_id': id,
        }
    
    return render(request, 'doctor_appointments.html', context)