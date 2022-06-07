from django.urls import path
from .views import *

urlpatterns = [
    path("", sockets, name="test-socket"),
    path("doctors/<id>/", doctor_room, name="doctor-room"),
    path("patients/<id>/", patient_room, name="patient-room"),
    
]

