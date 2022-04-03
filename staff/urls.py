from django.urls import path
from .views import *

urlpatterns = [
    path("", index, name="index"),
    
    ## doctors
    path("doctors", doctorsList, name="doctors-list"),
    path("doctors/<id>", doctorDetails, name="doctor-details"),
    path("doctors/verification/<id>", editVerification, name="edit-verification"),
    
    ## categories
    path("categories", medicalCategorylist, name="medical-categories"),
    path("categories/add", addCategory, name="add-category"),
    path("categories/edit/<id>", editCategory, name="edit-category"),
    
    ## medical tips
    path("medical-tips", medicalTipsList, name="medical-tips"),
    path("medical-tips/<id>", medicalTipDetails, name="medical-tips-details"),
    path("medical-tips/upload", uploadMedicalTip, name="upload-tip"),
    path("medical-tips/delete/<id>", deleteMedicalTip, name="delete-tip"),
    
]