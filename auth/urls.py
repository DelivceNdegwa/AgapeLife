from django.urls import path
from auth.views import AgapeUserObtainTokenPairView, AgapeUserRegisterView
from rest_framework_simplejwt.views import TokenRefreshView


urlpatterns = [
    path('login/', AgapeUserObtainTokenPairView.as_view(), name='token_obtain_pair'),
    path('login/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('register/', AgapeUserRegisterView.as_view(), name='auth_register'),
]