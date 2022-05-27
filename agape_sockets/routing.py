from django.urls import re_path, path
from .consumers import *

websocket_urlpatterns = [
   path('ws/chat/<str:room_name>/', ChatConsumer.as_asgi()),
   path('ws/doctor/appointments/<str:doctor_room>/', DoctorAppointmentConsumer.as_asgi()),
   path('ws/test/', TestSyncConsumer.as_asgi())
]