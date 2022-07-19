from django.db import models


class FCMToken(models.Model):
    
    DOCTOR = 1
    AGAPE_USER = 2
    
    USER_TYPES = (
        (DOCTOR, "Doctor"),
        (AGAPE_USER, "Agape User")
    )
    
    user_id = models.IntegerField()
    token = models.TextField()
    user_type = models.IntegerField(choices=USER_TYPES)

    def __str__(self):
        return self.token