from atexit import register
from django.contrib import admin
from .models import *

admin.site.register(DoctorCategoryPivot)
admin.site.register(UserFeedback)
admin.site.register(MedicalTips)
admin.site.register(Doctor)
admin.site.register(Appointment)
admin.site.register(PatientSymptoms)
admin.site.register(DoctorPrescriptions)
admin.site.register(AgapeUser)
admin.site.register(MedicalCategory)
admin.site.register(LoggedInDoctor)
admin.site.register(EditorDoctorPivot)
