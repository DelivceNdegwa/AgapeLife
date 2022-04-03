from django.urls import path
from .views import *

urlpatterns = [
    path('user_list', user_list, name="user_list")
]
