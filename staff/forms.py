# from socket import fromshare
from django import forms
from django.forms import ModelForm
from .models import MedicalCategory, MedicalTips


class MedicalTipsForm(forms.ModelForm):
    
    class Meta:
        model = MedicalTips
        fields = '__all__'
        

class MedicalCategoryForm(forms.ModelForm):
    class Meta:
        model = MedicalCategory
        fields = ['name']
        
        
