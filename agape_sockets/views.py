from django.shortcuts import render
from django.contrib.auth import get_user_model, login, logout
from django.contrib.auth.decorators import login_required
from django.contrib.auth.forms import AuthenticationForm, UserCreationForm
from django.urls import reverse 
from django.shortcuts import render, redirect

from django.http import JsonResponse

#################################
def test_sockets(request):
    return render(request, "sockets.html")