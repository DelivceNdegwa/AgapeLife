from celery import shared_task
from staff.models import AgapeUser, Doctor, Notification
from django_celery_beat.models import PeriodicTask

from asgiref.sync import async_to_sync
from channels.layers import get_channel_layer


# TODO: Create appointment_reminder for both doctor and patient parameters: receiver

# Remember: Groups= patient_{id}, doctor_{id}

@shared_task(bind=True)
def appointment_reminder(self, id, category, message):
    try:
        notification=Notification.objects.create(
            recipient_category=category, 
            recipient_id= id,
            message= message
        )
        
        return "Notification created"
    except Exception as e:
        return "ERROR: Notification error => {}".format(e)


        