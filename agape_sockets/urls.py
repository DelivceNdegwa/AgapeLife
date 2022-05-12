from django.urls import path
from .views import *

urlpatterns = [
    path("test-socket/", test_sockets, name="test-socket")
]

