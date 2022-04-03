from django.contrib.auth import user_logged_in, user_logged_out
from django.dispatch import receiver
from staff.models import LoggedInDoctor


@receiver(user_logged_in)
def on_user_login(sender, **kwargs):
    LoggedInDoctor.objects.get_or_create(user=kwargs.get('user'))


@receiver(user_logged_out)
def on_user_logout(sender, **kwargs):
    LoggedInDoctor.objects.filter(user=kwargs.get('user')).delete()
