from django.contrib import admin
from .models import TestSocketModel, TestSocketMonitor

admin.site.register(TestSocketModel)
admin.site.register(TestSocketMonitor)
