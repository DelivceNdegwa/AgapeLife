from __future__ import absolute_import, unicode_literals
import os

from pytz import timezone

from celery import Celery
from celery.schedules import crontab

from django.conf import settings


os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'agapeweb.settings')

app = Celery('agapeweb')

app.conf.enable_utc = False
app.conf.update(timezone='Africa/Nairobi')

app.config_from_object(settings, namespace='CELERY')

# Celery Beat
app.conf.beat_schedule = {
    "delete-occured-appointments":{
        "task": "agape_sockets.tasks.clean_notification_cronjobs",
        "schedule": crontab(minute='*')#crontab(hour=0, day_of_month='*', month_of_year='*')
    }
}

app.autodiscover_tasks()

@app.task(bind=True)
def debug_task(self):
    print(f'Request: {self.request!r}')

