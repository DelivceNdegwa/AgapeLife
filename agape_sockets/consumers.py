from concurrent.futures import thread
import json
from channels.generic.websocket import WebsocketConsumer, AsyncWebsocketConsumer
from channels.db import database_sync_to_async

from asgiref.sync import async_to_sync, sync_to_async

from staff.models import Appointment, AppointmentRequest, Doctor
from .models import AppointmentRequest1, TestSocketModel

from django.db.models.signals import post_save
from django.dispatch import receiver
from django.core import serializers

from agape_sockets import common_requirements


class DoctorAppointmentsConsumer(AsyncWebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(self, *args, **kwargs)
        
        
    async def connect(self):
        self.doctor_id = self.scope['url_route']['kwargs']['id']
        self.group_name = 'doctor_{}'.format(self.doctor_id)
        
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )
        
        await self.accept()
        print("INFO: CONNECTION FOR DoctorAppointmentsConsumer HAS BEEN ESTABLISHED")
        await self.load_appointments()
        
    async def disconnect(self, status_code):
        await self.channel_layer.group_discard(
            self.group_name,
            self.channel_name
        )
        
    async def load_appointments(self):
        doctor_appointments = await database_sync_to_async(list)(
                                            self.get_appointments()
                                        )
        print("NOW:",doctor_appointments)
        self.serialized_list = serializers.serialize('json', doctor_appointments)
        print(self.serialized_list)
        
        await self.send(text_data=json.dumps({
            "appointment_list":self.serialized_list
        }))
        
    
    async def created_appointment_listener(self, event):
        new_appointment = event['data']
        print('INFO: created_appointment_listener', new_appointment)  
        
        doctor_appointments = await database_sync_to_async(list)(
                                            self.get_appointments()
                                        )
        
        await self.send(text_data=json.dumps({
            "appointment_list": serializers.serialize('json', doctor_appointments),
            "new_appointment": True,
            "appointments_number": len(self.serialized_list)
        })) 
        
    async def updated_appointment_listener(self, event):
        updated_appointment = event['data']
        print('INFO: updated_appointment_listener', updated_appointment)
        
        await self.send(text_data=json.dumps({
            "appointment_list": self.serialized_list,
            "updated_item": updated_appointment,
            "updated_appointment": True,
        }))
        
    def get_appointments(self):
        return common_requirements.get_doctor_appointments(self.doctor_id)
        
    def messages_to_json(self, messages):
        result = []
        for message in messages:
            result.append(self.message_to_json(message))
        return result

    def message_to_json(self, message):
        return {
            'id': message.id,
            'client': message.client.id,
            'doctor': message.doctor.id,
            'about': message.about,
            'status': message.status,
            'read': message.read
        }
        

class PatientAppointmentsConsumer(AsyncWebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(self, *args, **kwargs)
        
    async def connect(self):
        self.patient_id = self.scope['url_route']['kwargs']['id']
        self.group_name = 'patient_{}'.format(self.patient_id)
        
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )
        
        await self.accept()
        print("INFO: CONNECTION FOR PatientAppointmentsConsumer HAS BEEN ESTABLISHED")
        
        await self.load_appointments()
        
    async def disconnect(self, status_code):
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )
        
    async def created_appointment_listener(self, event):
        appointment = event['data']
        print('INFO: CREATED DATA: ', appointment)
        print('INFO: BEFORE= ', len(self.serialized_appointments))
        
        # patient_appointments = await database_sync_to_async(list)(
        #                             self.patient_appointments()
        #                         )
        
        await self.send(text_data=json.dumps({
            "new_appointment":True
        }))
        await self.load_appointments()
        
    async def updated_appointment_listener(self, event):
        appointment = event["data"]
        
        print('INFO: UPDATED DATA: ', appointment)
        
        await self.send(text_data=json.dumps({
            "appointment_list":self.serialized_appointments,
            "updated_appointment":True
        }))
        
    def patient_appointments(self):
        print("PATIENT_ID", self.patient_id)
        return common_requirements.patient_appointments(self.patient_id)
    
    async def load_appointments(self):
        patient_appointments = await database_sync_to_async(list)(
                                    self.patient_appointments()
                                )
        print(patient_appointments)
        self.serialized_appointments = serializers.serialize('json', patient_appointments)
        print("INFO: SERIALIZED APPOINTMENTS ", self.serialized_appointments)
        
        await self.send(text_data=json.dumps({
            "appointment_list":self.serialized_appointments
        }))

    def messages_to_json(self, messages):
        result = []
        for message in messages:
            result.append(self.message_to_json(message))
        return result

    def message_to_json(self, message):
        return {
            'id': message.id,
            'title': message.title,
            'about': message.about,
            'start_time': message.start_time,
            'end_time': message.end_time,
            'doctor': message.doctor.id,
            'status': message.status,
        }
        
 

class OnlineDoctorsConsumer(AsyncWebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(self, *args, **kwargs)
        self.group_name = common_requirements.online_doctors_group
    
    async def connect(self):
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )   
        await self.accept()
        print("1. CONNECTED")
        await self.load_doctors()  
        
    async def disconnect(self, status_code):
        self.channel_layer.group_discard(
             self.group_name,
             self.channel_name
        ) 
        
    async def load_doctors(self):
        print("2. LOAD DOCTORS CALLED")
        online_doctors = await database_sync_to_async(list)(
                                self.get_online_doctors()
                            )
        self.serialized_list = self.messages_to_json(online_doctors)
        print("4. ONLINE_DOCTORS LIST:")
        print("INFO: LIST_LENGTH=", len(self.serialized_list))
        
        await self.send(text_data=json.dumps({
            "doctor_list": self.serialized_list,
            "doctor_number": len(self.serialized_list)
        }))
        
    async def reload_group(self, event):
        online_doctors = await database_sync_to_async(list)(self.get_online_doctors())
        self.serialized_list = self.messages_to_json(online_doctors)
        
        await self.send(text_data=json.dumps({
            "doctor_list": self.serialized_list,
            "doctor_number": len(self.serialized_list)
        }))
        
    async def update_listener(self, event):
        online_doctor = event["doctor"]
        print("INFO: update_listener function called")
        
        print("INFO: DOCTOR_ID=",online_doctor['id'])
        
        if online_doctor['is_available']:
            self.serialized_list.append(online_doctor)
            
        else:
            doctor_index = next((index for (index, item) in enumerate(self.serialized_list) if item['id'] == online_doctor['id']), None)
            print("INFO: FILTERED_ITEM=", doctor_index)
            del self.serialized_list[doctor_index]
        print("INFO: UPDATED_LIST_LENGTH=", len(self.serialized_list))   
        
        await self.send(text_data=json.dumps({
            "doctor_list": self.serialized_list,
            "doctor_number": len(self.serialized_list)
        }))

    def get_online_doctors(self):
        print("3. get_online_doctors() called")
        return common_requirements.get_online_doctors()

    def messages_to_json(self, messages):
        result = []
        for message in messages:
            result.append(self.message_to_json(message))
        return result

    def message_to_json(self, message):
        return {
            'id': message.id,
            'is_verified': message.is_verified,
            'is_available': message.is_available,
            'phone_number': message.phone_number,
            'id_number': message.id_number
        }
        
          
class PatientNotificationsConsumer(AsyncWebsocketConsumer):
    async def connect(self):
        self.patient_id = self.scope['url_route']['kwargs']['id']
        self.group_name = "notify_patient_{}".format(self.patient_id)
        
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )
        await self.accept()
        print("INFO: Connection established")
        
    async def disconnect(self, code):
        await self.channel_layer.group_discard(
            self.group_name, 
            self.channel_name
        )
        
    async def patient_notification_listener(self, event):
        notification = event["notification"]
        print("PATIENT_NOTIFICATION: ", notification)
        await self.send(text_data=json.dumps({
            "notification": notification
        }))
      
        
class DoctorNotificationsConsumer(AsyncWebsocketConsumer):
    async def connect(self):
        self.doctor_id = self.scope['url_route']['kwargs']['id']
        self.group_name = 'notify_doctor_{}'.format(self.doctor_id)
        
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )
        
        await self.accept()
        print("Doctor connection established")
    async def disconnect(self, code):
        await self.channel_layer.group_discard(
            self.group_name,
            self.channel_name
        )
        
    async def doctor_notification_listener(self, event):
        notification = event['notification']
        print("DOCTOR_NOTIFICATION: ", notification)
        await self.send(text_data=json.dumps({
            "notification": notification
        }))