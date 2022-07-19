from pusher_push_notifications import PushNotifications


class AppointmentNotifications():
    def __init__(self):
        self.__instance_id = '625de246-14e9-495a-af65-a11f8062d8d4'
        self.__secret_key = '7AEA331C3980C84BBB994B786F98C366BD2AAF820E8000758D1F0733063D37F6'
        
    def get_instance_id(self):
        return self.__instance_id
    
    def get_key(self):
        return self.__secret_key
        
    def create_push_notification(self, title, body):
        self.title = title
        self.body = body
        
        pn_client = PushNotifications(
            instance_id= self.__instance_id,
            secret_key=self.__secret_key,
        )

        response = pn_client.publish(
            interests=['hello'],#appointment_notification
            publish_body={
                'apns':{
                    'aps': {'alert': self.title}
                    },
                
                'fcm':{
                    'notification':{
                            'title': self.title,
                            'body': self.body
                        }
                    }
            }
        )        
        
        print(f"Publish ID: {response['publishId']}")