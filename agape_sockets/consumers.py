from cgitb import text
import json
from channels.generic.websocket import AsyncWebsocketConsumer, WebsocketConsumer
from django.core import serializers

from staff.models import AppointmentRequest
from .models import TestSocketModel

from .consumer_queries import *

from asgiref.sync import sync_to_async

from channels.db import database_sync_to_async


from django.db.models.signals import post_save
from django.dispatch import receiver

from channels.layers import get_channel_layer
from asgiref.sync import async_to_sync

class ChatConsumer(AsyncWebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(args, kwargs)
        self.room_group_name = None
        self.room_name = None

    async def connect(self):
        self.room_name = self.scope['url_route']['kwargs']['room_name']
        self.room_group_name = 'chat_%s' % self.room_name

        # Join room group
        await self.channel_layer.group_add(
            self.room_group_name,
            self.channel_name
        )

        await self.accept()

    async def disconnect(self, close_code):
        # Leave room group
        await self.channel_layer.group_discard(
            self.room_group_name,
            self.channel_name
        )

    # Receive message from room group
    async def chat_message(self, event):
        message = event['message']
        
        # Send message to WebSocket
        await self.send(text_data=json.dumps({
            'message': message
        }))
        
    async def observe_monitor(self, event):
        message = event["message"]
        print(message)
        
        await self.send(text_data=json.dumps({
            'message': message
        }))
        

class DoctorAppointmentConsumer(AsyncWebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.group_name = None
        
    async def connect(self):
        self.group_name = self.scope['url_route']['kwargs']['doctor_room']
        
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )
        await self.accept()
        
    async def disconnect(self, code):
        await self.channel_layer.group_discard(
            self.group_name,
            self.channel_name
        )

    # def get_total_appointment_requests():
    #     # return AppointmentRequest.objects.select_related('doctor').filter(
    #     #                     doctor__id=id, status=AppointmentRequest.PENDING
    #     #     )
    #     return Appointment.objects.all()

    
    # Listens to created Doctor AppointmentRequest instances from AppointmentRequest signal
    async def appointments_listener(self, event):
        doctor_id = event['doc_id']
        print("DOCTOR_ID:", doctor_id)
        
        messages = await database_sync_to_async(AppointmentRequest.objects.all)()
        
        json_messages = await self.messages_to_json(messages)
        
        # print(messages)
        
        await self.send(text_data=json.dumps({
            'messages': messages
        }))
        
    async def messages_to_json(self, messages):
        result = []
        for message in messages:
            result.append(self.message_to_json(message))
        print("RESULT", result)
        return result

    async def message_to_json(self, message):
        return {
            'id': message.id,
            'client': message.client,
            'about': message.about,
            'symptoms': message.symptoms,
        }


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
        
        self.send(text_data=json.dumps({
                "message":serializers.serialize('json', TestSocketModel.objects.all())
            }))

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
