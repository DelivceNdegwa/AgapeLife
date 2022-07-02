import json
import datetime
from datetime import timedelta

from django_celery_beat.models import PeriodicTask, CrontabSchedule, IntervalSchedule


def createPeriodicTask(cron_time, receiver_id, receiver_type, message):
    print("CRON_TIME: ", cron_time.minute)
    current_time = datetime.datetime.now()
    time_range_from_current_time = cron_time - current_time
    
    if(time_range_from_current_time > timedelta(minutes=10)):
        cron_time -= timedelta(minutes=10)
    
    # cron_time = start_time - timedelta(minutes=5)
    cron_job, _ = CrontabSchedule.objects.get_or_create(
                            minute=cron_time.minute,
                            hour=cron_time.hour,
                            day_of_month=cron_time.day,
                            month_of_year=cron_time.month
                        )
    
    schedule, created = IntervalSchedule.objects.get_or_create(
                            every=10,
                            period=IntervalSchedule.SECONDS,
                        )
                                # id, category, message
        
    PeriodicTask.objects.create(
        crontab= cron_job,
        name='doctor_notification_{}_{}'.format(receiver_id, cron_time),
        task='agape_sockets.tasks.appointment_reminder', 
        args= json.dumps((receiver_id, receiver_type, message)),
        one_off= True
    )
    

def createBirthdayScheduler(user, user_type):
    birth_date = user.date_of_birth
    
    cron_job, _ = CrontabSchedule.objects.get_or_create(
                        minute=0,
                        hour='0, 6, 12, 18, 21',
                        day_of_month= birth_date.day,
                        month_of_year= birth_date.month
                 )
                
    PeriodicTask.objects.create(
        crontab = cron_job,
        name = f'birthday_reminder_{user.username}',
        task = 'agape_sockets.tasks.birthday_reminder',
        args = json.dumps((user.id, user_type, user.first_name))
    )
        
    
