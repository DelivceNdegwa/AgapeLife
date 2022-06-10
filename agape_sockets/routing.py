from django.urls import re_path, path
from .consumers import *

websocket_urlpatterns = [
   path('ws/doctor/<id>/appointments/', DoctorAppointmentsConsumer.as_asgi()),
   path('ws/patient/<id>/appointments/', PatientAppointmentsConsumer.as_asgi()),
   path('ws/patient/<id>/notifications/', PatientNotificationsConsumer.as_asgi()),
   path('ws/doctor/<id>/notifications/', DoctorNotificationsConsumer.as_asgi()),
   path('ws/online-doctors/', OnlineDoctorsConsumer.as_asgi()),
]