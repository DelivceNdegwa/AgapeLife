from datetime import datetime
from celery import shared_task
from staff.models import AgapeUser, Doctor, Notification
from django_celery_beat.models import PeriodicTask, CrontabSchedule

from asgiref.sync import async_to_sync
from channels.layers import get_channel_layer


# TODO: Create appointment_reminder for both doctor and patient parameters: receiver

# Remember: Groups= patient_{id}, doctor_{id}

@shared_task(bind=True)
def appointment_reminder(self, id, category, message):
    try:
        Notification.objects.create(
            recipient_category=category, 
            recipient_id= id,
            message= message
        )
        
        return "Notification created"
    except Exception as e:
        return "ERROR: Notification error => {}".format(e)
    
@shared_task(bind=True)
def say_hi(self):
    print("Hiiiii")
    return "Done"

@shared_task(bind=True)
def clean_notification_cronjobs(self):
    current_date = datetime.date()
    cronjobs = CrontabSchedule.objects.filter(day_of_month=current_date.day, month_of_year=current_date.month)
    try:
        for job in cronjobs:
            job.delete()
            print("{} deleted".format(job.id))
        return "Done"        
    except Exception as e:
        print("DELETION_ERROR: ", e)
        return "Failed" 


        