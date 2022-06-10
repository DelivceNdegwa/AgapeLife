from django.db import models
from django.contrib.auth.models import User

from django.db.models.signals import post_save

from django.dispatch import receiver
from channels.layers import get_channel_layer

from staff.models import *

from asgiref.sync import async_to_sync

import json

class TestSocketMonitor(User):
    class Meta:
        verbose_name = 'Test User Monitor'
    def __str__(self):
        return self.username
class TestSocketModel(models.Model):
    READ = 1
    UNREAD = 2
    
    STATUS_CHOICES = (
        (READ, "READ"),
        (UNREAD, "UNREAD")
    )
    
    name = models.CharField(max_length=30)
    status = models.IntegerField(choices=STATUS_CHOICES, default=UNREAD)
    user = models.ForeignKey(TestSocketMonitor, on_delete=models.CASCADE, null=True, blank=True)
    
    def __str__(self):
        return self.name

    def getStatus(self):
        if self.status == self.READ:
            return "READ"
        return "UNREAD" 
    




