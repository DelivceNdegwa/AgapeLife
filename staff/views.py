from django.shortcuts import render
from django.http import JsonResponse, HttpResponse

from django.core.exceptions import ObjectDoesNotExist
from django.db.models import ProtectedError

# from django.core.servers.basehttp import FileWrapper #django <=1.8
from wsgiref.util import FileWrapper 
import mimetypes
import os

from pathlib import Path

from django.utils.encoding import  smart_str

from .models import *
from .forms import *


## navigation to all content i.e doctors section, tips section and feedbacks section
def index(request): 
    categories = MedicalCategory.objects.all()
    doctors = Doctor.objects.all()
    
    context={
        "number_of_doctors":doctors.count(),
        "doctors":doctors,
        "categories":categories,
        "new_feedbacks":UserFeedback.objects.filter()
        
    }
    return render(request, "index.html", context)

## Doctor views
def doctorsList(request):
    doctors = Doctor.objects.all()
    context = {
        "doctors":doctors
    }
    
    return render(request, "doctors/doctors-list.html", context)

def doctorDetails(request,id):
    doctor = Doctor.objects.filter(id=id).first()
    context = {
        "doctor":doctor,
    }
    return render(request, "doctors/doctor-details.html", context)

def verifyDoctor(request, id):
    
    success = False
    message = "Something went wrong"
    
    if request.method == "POST":
        doctor = Doctor.objects.filter(id=id).first()
        
        doctor.is_verified = True 
        doctor.save()   
        
        success = True
        message = "You have verified Dr "+doctor.first_name
        
    data = {
        "success":success,
        "message":message,
    }
    
    return JsonResponse(data)

def deverifyDoctor(request, id):
    
    success = False
    message = "Something went wrong"
    
    if request.method == "POST":
        doctor = Doctor.objects.filter(id=id).first()
        
        print(":::"+str(doctor.id)+":::")
        
        doctor.is_verified = False
        doctor.save()
        success = True
        message = "You have deverified Dr "+doctor.first_name
        
    data = {
        "success":success,
        "message":message,
    }
    
    return JsonResponse(data)

## Medical categories
def medicalCategorylist(request):
    categories = MedicalCategory.objects.all()
    
    context = {
        "categories":categories,
    }

    return render(request, "categories/categories.html", context)

def addCategory(request):
    form = MedicalCategoryForm()
    success=False
    if request == 'POST':
        message="Something went wrong"
        form = MedicalCategoryForm(request.POST)
        if form.is_valid():
            form.save()
            success=True
            message="Category added successfully"
            
    data = {
        "success":success,
        "message":message
    }  
    return JsonResponse(data)
            
def editCategory(request,id):
    success=True
    try:
        category = MedicalCategory.objects.filter(id=id).first()
    except ObjectDoesNotExist as e:
        message=e
        success=False
    
    if request.method == 'POST':
        edited_name = request.POST.get('category-name')
        category.name = edited_name
        category.save()
        
        message= "Category updated successfully"
    else:
        success=False
        message='Request was not post'
    
    data = {
        "success":success,
        "message":message,
    }    
    
    return JsonResponse(data)

    
## Medical tips views    
def medicalTipsList(request):
    medical_tips = MedicalTips.objects.all()
    context = {"medical_tips":medical_tips}
    
    return render(request, "medical-content/content-list.html", context)
           
def medicalTipDetails(request, id):
    medical_tip = MedicalTips.objects.filter(id=id).first() 
    context = {"tip": medical_tip}  
    
    return render(request, "medical-content/content-details.html", context)     
           
def uploadMedicalTip(request):
    if request.method == 'POST':
        form = MedicalTips(request.POST, request.FILES)
        
        if form.is_valid():
            form.save()
            success=True
            
        else:
            success=False
            
    else:
        success=False
        form = MedicalTips()
        
    return JsonResponse({'success':success})

def deleteMedicalTip(request, id):
    success=True
    try:
        tip_instance = MedicalTips.objects.filter(id=id).first()
        
    except ObjectDoesNotExist as e:
        success=False
        message = e
    
    if request.method == 'POST':
        try:
            tip_instance.delete()
            message="Medical tip was successfully deleted"
            
        except ProtectedError as pe:
            message=pe
            success=False
            
    else:
        success=False
        message="POST request required"
        
    data = {
        "success":success,
        "message":message,
    }
        
    return JsonResponse(data)
        

       
        