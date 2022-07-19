import firebase_admin
from firebase_admin import credentials, messaging


firebase_json_conf_path = "../google-credentials/agapelifepushnotifications-firebase-adminsdk-ox8wy-35ceab6006.json"

cred = credentials.Certificate(firebase_json_conf_path)
firebase_admin.initialize_app(cred)


def sendPushNotification(title, message, registration_token, dataObject=None):
    # Defining the message payload for our notification
    message = messaging.MulticastMessage(
        notification=messaging.Message(
            title=title,
            body=message
        ),
        data=dataObject,
        tokens=registration_token
    )
    try:
        # Sending the notification to the device with the corresponding provided registration
        response = messaging.send_multicast(message)
        print(f"Successfully sent {response}")
    except Exception as e:
        print(f"fcm_error:{e}")
