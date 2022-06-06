from django.urls import re_path, path
from .consumers import *

websocket_urlpatterns = [
   path('ws/doctor/<id>/appointments/', DoctorAppointmentsConsumer.as_asgi()),
   path('ws/patient/<id>/appointments/', PatientAppointmentsConsumer.as_asgi()),
   path('ws/test/', OnlineDoctorsConsumer.as_asgi())
]