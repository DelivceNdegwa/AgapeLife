import json
from channels.generic.websocket import AsyncWebsocketConsumer
from channels.db import database_sync_to_async

from django.core import serializers

from agape_sockets import common_requirements

from asgiref.sync import sync_to_async


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
        await self.load_appointment_requests()
        
    async def disconnect(self, status_code):
        await self.channel_layer.group_discard(
            self.group_name,
            self.channel_name
        )
        
    async def load_appointment_requests(self):
        doctor_appointments = await database_sync_to_async(list)(
                                            self.get_appointment_requests()
                                        )
        
        self.serialized_list = self.appointment_requests_messages_to_json(doctor_appointments)
        print(self.serialized_list)
        
        await self.send(text_data=json.dumps({
            "appointment_request_list":self.serialized_list
        }))
        
    async def load_appointments(self):
        doctor_appointments = await database_sync_to_async(list)(
                                            self.get_upcoming_appointments()
                                        )

        self.appointments_list = self.appointment_messages_to_json(doctor_appointments)
        print(self.appointments_list)
        
        await self.send(text_data=json.dumps({
            "appointment_list": self.appointments_list
        }))
    
    async def created_appointment_listener(self, event):
        new_appointment = event['data']
        print('INFO: created_appointment_listener', new_appointment)  
        
        doctor_appointments = await database_sync_to_async(list)(
                                            self.get_appointment_requests()
                                        )
        
        await self.send(text_data=json.dumps({
            "appointment_list": self.appointment_requests_messages_to_json(doctor_appointments),
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
        
    def get_appointment_requests(self):
        return common_requirements.get_doctor_appointment_requests(self.doctor_id)
        
    def get_upcoming_appointments(self):
        return common_requirements.get_doctor_upcoming_appointment(self.doctor_id)  
        
    def appointment_requests_messages_to_json(self, messages):
        result = []
        for message in messages:
            result.append(self.appointment_requests_message_to_json(message))
        return result
    
    def appointment_messages_to_json(self, messages):
        result = []
        for message in messages:
            result.append(self.appointment_message_to_json(message))
        return result
    
    def appointment_message_to_json(self, message):
        return {
            'id': message.id,
            'title': message.title,
            'about': message.about,
            'start_time': str(message.start_time),
            'end_time': str(message.end_time),
            'doctor': message.doctor_id,
            'client': message.client_id,
            'status': message.status 
        }

    def appointment_requests_message_to_json(self, message):
        return {
            'id': message.id,
            'client': message.client_id,
            'doctor': message.doctor_id,
            'about': message.about,
            'status': message.status,
            'symptoms': message.symptoms,
            'persistence_period': message.persistence_period,
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
        
    async def load_appointments(self):
        patient_appointments = await database_sync_to_async(list)(
                                    self.patient_appointments()
                                )
        print(patient_appointments)
        self.serialized_appointments = self.messages_to_json(patient_appointments)#serializers.serialize('json', patient_appointments)
        print("INFO: SERIALIZED APPOINTMENTS ", self.serialized_appointments)
        

        await self.send(text_data=json.dumps({
            "appointment_list":self.serialized_appointments
        }))
        
    def patient_appointments(self):
        print("PATIENT_ID", self.patient_id)
        return common_requirements.patient_appointments(self.patient_id)

    def messages_to_json(self, messages):
        result = []
        for message in messages:
            result.append(self.message_to_json(message))
        return result

    def message_to_json(self, message):
        print("TIMES::::::>",message.end_time)
        return {
            'id': message.id,
            'title': message.title,
            'about': message.about,
            'start_time': str(message.start_time),
            'end_time': str(message.end_time),
            'doctor': message.doctor_id,
            'status': message.status,
        }
        

class OnlineDoctorsConsumer(AsyncWebsocketConsumer):
    def __init__(self, *args, **kwargs):
        super().__init__(self, *args, **kwargs)
    
    async def connect(self):
        self.group_name = common_requirements.online_doctors_group
        
        await self.channel_layer.group_add(
            self.group_name,
            self.channel_name
        )   
        
        await self.accept()
        print("1. CONNECTED")
        await self.load_doctors()  
        
    async def disconnect(self):
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
            # "doctor_number": len(self.serialized_list)
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
        await self.load_doctors()
        

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
            'id_number': message.id_number,
            'profile_image': str(message.profile_image),
            'experience_years': message.experience_years,
            'first_name': message.first_name,
            'last_name': message.last_name,
            'hospital': message.hospital,
            'specialization': message.speciality
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