from datetime import datetime, date
from celery import shared_task
from firebase_tokens.models import FCMToken

from staff.models import AgapeUser, Doctor, Notification
from django_celery_beat.models import PeriodicTask, CrontabSchedule

from asgiref.sync import async_to_sync
from channels.layers import get_channel_layer

from fcm_admin import fcm_config as fcm


# TODO: Create appointment_reminder for both doctor and patient parameters: receiver

# Remember: Groups= patient_{id}, doctor_{id}

@shared_task(bind=True)
def appointment_reminder(self, id, category, message):
    try:
        # instance= Notification.objects.create(
        #             recipient_category=category, 
        #             recipient_id= id,
        #             message= message
        #         )
        
        fcm_instance = FCMToken.objects.filter(user_id=id, user_type=category).first()
        
        fcm.sendPushNotification(
            "Appointment reminder",
            message,
            fcm_instance.token
        )
        
        return "Notification has been sent"
    except Exception as e:
        return "ERROR: Notification error => {}".format(e)
    
@shared_task(bind=True)
def birthday_reminder(self, id, category, name):
    message = f"Happy birthday {name}🎉🥳🎂"
    if(category == Notification.DOCTOR):
        message += " Thank you for being part of the Agape Life doctor family. Looking forward to your success in your new year."
    else:
        message += " Agape Life continues to be always with you even in your new year. Looking forward offering better services for you."
    
    try:
        instance =Notification.objects.create(
            recepient_category = category,
            recepient_id = id,
            message = message
        )
        return "Birthday notification created successfully"
    except Exception as e:
        return f"ERROR: Birthday notification error => {e}"
    
@shared_task(bind=True)
def say_hi(self):
    print("Hiiiii")
    return "Done"

@shared_task(bind=True)
def clean_notification_cronjobs(self):
    current_date = date.today()
    print("NUMBER_OF_JOBS: {}".format(CrontabSchedule.objects.all().count()))
    
    cronjobs = CrontabSchedule.objects.filter(day_of_month=current_date.day, month_of_year=current_date.month)
    periodic_tasks = PeriodicTask.objects.select_related('crontab').filter(crontab__in=cronjobs)    
        
    try:
        for periodic_task in periodic_tasks:
            periodic_task.delete()
            print(f"Deleted task: {periodic_task}")
            
        for job in cronjobs:
            job.delete()
            print("{} deleted".format(job))
            
    except Exception as e:
        return f"CLEAN NOTIFICATION CRONJOBS ERROR: {e}"
    
    print("NUMBER_OF_JOBS: {}".format(CrontabSchedule.objects.all().count()))
    return "Done" 

        