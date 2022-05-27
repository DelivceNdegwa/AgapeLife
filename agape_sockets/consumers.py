import json
from channels.generic.websocket import WebsocketConsumer
from django.core import serializers

from staff.models import AppointmentRequest
from .models import TestSocketModel


from django.db.models.signals import post_save
from django.dispatch import receiver
from asgiref.sync import async_to_sync


class TestSyncConsumer(WebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.group_name = None
        self.result = []
        
    def connect(self):
        self.group_name = "important_notifications"
        
        async_to_sync(self.channel_layer.group_add)(
            self.group_name,
            self.channel_name
        )
        
        self.accept()
        self.load_messages()
        
    def disconnect(self, status_code):
        async_to_sync(self.channel_layer.group_discard)(
            self.group_name,
            self.channel_name
        )
        
        
    def load_messages(self):
        self.send(text_data=json.dumps({
            "message": serializers.serialize('json', TestSocketModel.objects.all())
        }))
        
        
    # Test 1
    @receiver(post_save, sender=TestSocketModel, dispatch_uid='test_socket_model_change_listener')
    def test_socket_model_change_listener(self, sender, instance, created, **kwargs):
        if instance:
            if created:
                log_message = "{} created".format(instance.id)
            else:
                log_message = "{} updated".format(instance.id)
                
            print(log_message)
            print("IMPORTANT NOTIFICATION SENDING...")
            message = {
                'id': instance.id,
                'name': instance.name,
                'status': instance.getStatus(),
                'user': instance.user.username,
            }
            
            async_to_sync(self.channel_layer.group_send)(
                self.group_name,
                {
                    'type': 'received_message',
                    'message': instance
                }
            )
            
            print("IMPORTANT NOTIFICATION SENT")


    def received_message(self, event):
        print("received_message called")
        message = event['message']
        
        print("RECEIVED MESSAGE", message)
        
        try:
            self.send(text_data=json.dumps({
                "message":serializers.serialize('json', TestSocketModel.objects.all())
            }))
            print("SOCKET MESSAGE SENT SUCCESSFULLY")
        except Exception as e:
            print("ERROR:::", e)
        

    def messages_to_json(self, messages):
        for message in messages:
            self.result.append(self.message_to_json(message))
        print("RESULT", self.result)
        return self.result

    def message_to_json(self, message):
        return {
            'id': message.id,
            'client': message.client,
            'about': message.about,
            'symptoms': message.symptoms,
        }   

class DoctorAppointmentsConsumer(WebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(self, *args, **kwargs)
        self.doctor_id = None
        
    def connect(self):
        print("INITIALIZING CONNECTION... ")
        
        self.doctor_id = self.scope['url_route']['kwargs']['id']
        self.group_name = "doctor_{}".format(self.doctor_id)
        
        try:
            async_to_sync(self.channel_layer.group_add)(
                self.group_name,
                self.channel_name
            )
            self.accept()
            print("Connection established for group:", self.group_name)
            self.load_messages()
            
        except Exception as e:
            print("CONNECTION ERROR:", e)
            
    def disconnect(self, status):
        async_to_sync(self.channel_layer.group_discard)(
            self.group_name,
            self.channel_name
        )
        
    def load_messages(self):
        appointment_requests = AppointmentRequest.objects.select_related('doctor').filter(
                            doctor__id=self.doctor_id, status=AppointmentRequest.PENDING
                        )
        self.send(text_data=json.dumps({
            "message": serializers.serialize('json', appointment_requests)
        }))
        