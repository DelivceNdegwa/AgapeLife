import fcm_config as fcm

token = ""

fcm.sendPushNotification(
    "Appointment reminder",
    "You are having an appointment at exactly 10 minutes from now",
    token
)