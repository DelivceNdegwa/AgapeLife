from django.urls import path
from .views import *

urlpatterns = [
    path("categories/",MedicalCategoryListView.as_view()),
    path("categories/<int:pk>", MedicalCategoryDetailsView.as_view()),
    path("doctors/", DoctorListView.as_view()),
    path("doctors/<int:pk>", DoctorDetailsView.as_view()),
    path("users/", AgapeUserListView.as_view()),
    path("users/<int:pk>", AgapeUserDetailView.as_view()),
    path("feedbacks/", UserFeedBackListView.as_view()),
    path("feedbacks/<int:pk>", UserFeedBackDetailView.as_view()),
    path("health-tips/", MedicalTipsListView.as_view()),
    path("health-tips/<int:pk>", MedicalTipsDetailView.as_view()),
]