from django.shortcuts import render

from django.views.decorators.csrf import csrf_exempt

from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status

from firebase_tokens.models import FCMToken

from fcm_admin import fcm_config as fcm

# Create your views here.
@csrf_exempt
@api_view(["POST"])
def postFCMToken(request):  
    user_id = int(request.POST.get("user_id"))
    registration_token = request.POST.get("reg_token")
    user_type = request.POST.get("user_type")
    
    try:
        fcm_token_instance = FCMToken(
                            user_id=user_id,
                            token=registration_token,
                            user_type=user_type
                        )
    
        fcm_token_instance.save()
        
        return Response({"success_msg":"Successfully registered token"}, status=status.HTTP_200_OK)
    
    except Exception as e:
        return Response({"error_msg":str(e)}, status=status.HTTP_400_BAD_REQUEST)
    
    
    
    
    
    