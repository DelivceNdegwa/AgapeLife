from datetime import datetime

from django.db.models.signals import post_save, pre_save
from django.dispatch import receiver

from channels.layers import get_channel_layer
from asgiref.sync import async_to_sync

from .models import TestSocketModel, TestSocketMonitor
from staff.models import Appointment, LoggedInDoctor, AppointmentRequest, Doctor, Notification

from agape_sockets import common_requirements

@receiver(pre_save, sender=Doctor, dispatch_uid="online_doctor_list_listener")
def online_doctor_list_listener(sender, instance, **kwargs):
    channel_layer = get_channel_layer()
    
    if instance:
        instance_before = Doctor.objects.get(id=instance.id)
        
        if instance.is_verified:
            print("INFO: An update is taking place")
            
            updated_doctor = {
                'id': instance.id,
                'is_verified': instance.is_verified,
                'is_available': instance.is_available,
                'phone_number': instance.phone_number,
                'id_number': instance.id_number
            }
            
            if instance_before.is_available != instance.is_available:
                print("INFO: Availability changed from {} to {}".format(instance_before.is_available, instance.is_available))
                
                async_to_sync(channel_layer.group_send)(
                    common_requirements.online_doctors_group,
                    {
                        "type": "update.listener",
                        "doctor": updated_doctor
                    }
                )
                
            elif instance_before.is_verified != instance.is_verified and instance.is_verified:
                async_to_sync(channel_layer.group_send)(
                    common_requirements.online_doctors_group,
                    {
                        "type":"update.listener",
                        "doctor": updated_doctor
                    }
                )
                
            else:
                print("INFO: Changes were not relevant for this context")
        

@receiver(post_save, sender=AppointmentRequest, dispatch_uid="doctor_appointment_listener")
def doctor_appointment_listener(sender, created, instance, **kwargs):
    if instance:
        doctor_group_name = 'doctor_{}'.format(instance.doctor.id)
        
        channel_layer = get_channel_layer()
        
        message = {
            'id': instance.id,
            'client': instance.client.id,
            'doctor': instance.doctor.id,
            'about': instance.about,
            'status': instance.status,
            'read': instance.read
        }
        
        if created:
            status="created"
            consumer_function = 'created_appointment_listener'

        else:
            status="updated"
            consumer_function = 'updated_appointment_listener'
            
            if instance.status == 1:
                Appointment.objects.create(
                    title='A TEST APPOINTMENT',
                    about='Medical stuff',
                    start_time= datetime.now(),
                    end_time=datetime.now(),
                    doctor=instance.doctor,
                    client=instance.client,
                    status=Appointment.PENDING
                )
                print("NEW APPOINTMENT CREATED")
            
        print("INFO: Appointment {}".format(status))
        
        async_to_sync(channel_layer.group_send)(
            doctor_group_name,
            {
                'type':consumer_function,
                'data':message
            }
        )
        

@receiver(post_save, sender=Appointment, dispatch_uid='client_appointment_listener')
def client_appointment_listener(sender, created, instance, **kwargs):
        if instance:
            client_group_name = 'patient_{}'.format(instance.client.id)  
            channel_layer = get_channel_layer()
            
            message = {
                'id': instance.id,
                'title': instance.title,
                'about': instance.about,
                'doctor': instance.doctor.id,
                'status': instance.status,
            }
            
            if created:
                status="created"
                consumer_function = 'created_appointment_listener'

            else:
                status="updated"
                consumer_function = 'updated_appointment_listener'
                
            print("INFO: Appointment {}".format(status))
            
            async_to_sync(channel_layer.group_send)(
                client_group_name,
                {
                    'type':consumer_function,
                    'data':message
                }
            )
            
@receiver(pre_save, sender=Notification, dispatch_uid='notification_listener')
def notification_listener(sender, instance, **kwargs):
    if instance:
        print("SIGNAL_INFO: New instance created")
        
                
        message = {
            "message":instance.message,
            "recipient_id": instance.recipient_id,
            "recipient_category": instance.recipient_category
        }
        
        channel_layer = get_channel_layer()
        if instance.recipient_category == Notification.DOCTOR:      
            group_name = "notify_doctor_{}".format(instance.recipient_id)
            type_function = "doctor_notification_listener"
            

            async_to_sync(channel_layer.group_send)(
                group_name,
                {
                    "type": type_function,
                    "notification":message
                }
            )
            print("SIGNAL_INFO: data sent successfully")
            

            print("SIGNAL_INFO: Data will be sent to "+group_name) 
            
        else:
            group_name = "notify_patient_{}".format(instance.recipient_id)
            type_function = "patient_notification_listener"
        
            try:
                async_to_sync(channel_layer.group_send)(
                    group_name,
                    {
                        "notification":message,
                        "type": type_function
                    }
                )
                print("SIGNAL_INFO: data sent successfully")
            
            except Exception as e:
                print("SIGNAL_ERROR: ", e)
            print("SIGNAL_INFO: Data will be sent to "+group_name)  



        
        
        
