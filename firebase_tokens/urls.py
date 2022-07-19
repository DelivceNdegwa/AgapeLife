from urllib.parse import urlparse
from django.urls import path
from .views import postFCMToken

urlpatterns = [
    path('create/', postFCMToken)
]
