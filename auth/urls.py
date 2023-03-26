from django.urls import path
from auth.views import AgapeUserObtainTokenPairView, AgapeUserRegisterView, doctorFormRegister, uploadDoctorFiles, registerInstantPatient
from rest_framework_simplejwt.views import TokenRefreshView


urlpatterns = [
    path('login', AgapeUserObtainTokenPairView.as_view(), name='token_obtain_pair'),
    path('login/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('register', AgapeUserRegisterView.as_view(), name='auth_register'),
    path('doctor/form/register/', doctorFormRegister, name='register_doctor'),
    path('doctor/files/<id>', uploadDoctorFiles, name="upload_doc_files"),
    path('patient/register', registerInstantPatient, name="register-patient")
]