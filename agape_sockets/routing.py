from django.urls import re_path, path
from .consumers import *

websocket_urlpatterns = [
   path('ws/doctor/appointments/<id>/', DoctorAppointmentsConsumer.as_asgi()),
   path('ws/test/', TestSyncConsumer.as_asgi())
]