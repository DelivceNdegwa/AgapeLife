from django.urls import path
from .views import *

urlpatterns = [
    path("categories/",MedicalCategoryListView.as_view()),
    path("categories/<int:pk>", MedicalCategoryDetailsView.as_view()),
    
    path("doctors/", DoctorListView.as_view()),
    path("doctors/<int:id_number>", DoctorDetailsView.as_view()),
    path("doctors/edit-status/<int:id_number>", editOnlineStatus),
    
    
    path("users/", AgapeUserListView.as_view()),
    path("users/<int:id_number>", AgapeUserDetailView.as_view()),
    
    path("doctor-appointments/<int:pk>", DoctorAppointmentsListView.as_view()),
    path("client-appointments/<int:pk>", UserAppointmentsListView.as_view()),
    
    path("feedbacks/", UserFeedBackListView.as_view()),
    path("feedbacks/<int:pk>", UserFeedBackDetailView.as_view()),
    
    path("health-tips/", MedicalTipsListView.as_view()),
    path("health-tips/<int:pk>", MedicalTipsDetailView.as_view()),
    
    path("appointments/", AppointmentListView.as_view()),
    path("appointments/<int:pk>", AppointmentDetailView.as_view()),
    path("accept-appointment/", createAppointment),
    
    path("appointment-requests/<int:id_number>", AppointmentRequestListView.as_view()),
    path("appointment-requests/<int:pk>", AppointmentRequestDetailView.as_view()),
    path("book-appointment/", bookAppointment),
    
    path("get-patients/<int:id_number>", DoctorPatientsView.as_view()),
    
    path("generate-meeting-tokens/<int:pk>", generateTokens),
    
    # path('reg-doc/', testMultipartMap),
    # path('doctor/files/<id>', uploadDoctorFiles)
    
]
