from django.urls import path
from .views import *

urlpatterns = [
    path("", sockets, name="test-socket"),
    path("<str:id>/", room, name="room")
]

