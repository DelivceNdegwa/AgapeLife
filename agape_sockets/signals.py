from django.db.models.signals import post_save
from django.dispatch import receiver

from channels.layers import get_channel_layer
from asgiref.sync import async_to_sync

from .models import TestSocketModel, TestSocketMonitor
from staff.models import LoggedInDoctor, AppointmentRequest

# Socket tests
# Test 1
@receiver(post_save, sender=TestSocketModel, dispatch_uid='test_socket_model_change_listener')
def test_socket_model_change_listener(sender, instance, created, **kwargs):
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
        
        channel_layer = get_channel_layer()
        
        async_to_sync(channel_layer.group_send)(
            'important_notifications',
            {
                'type': 'received_message',
                'message': message
            }
        )
        
        print("IMPORTANT NOTIFICATION SENT")

# Test 2    
@receiver(post_save, sender=TestSocketMonitor, dispatch_uid="test_socket_monitor_change_listener")
def test_socket_monitor_change_listener(sender, instance, created, **kwargs):
    if instance:
        log_message = "{} updated".format(instance.id)
        
        if created:
            log_message = "{} created".format(instance.id)
        
        print(log_message)
        
        message = {
            'id': instance.id,
            'user': instance.username,
            'email': instance.email
        }
        
        channels_layer = get_channel_layer()
        
        async_to_sync(channels_layer.group_send)(
            'chat_random',
            {
                'type': 'observe_monitor',
                'message': message
            }
        )
        

# AppointmentRequests signal
@receiver(post_save, sender=AppointmentRequest, dispatch_uid="appointment_requests_change_listener")
def appointment_requests_change_listener(sender, instance, created, **kwargs):
    if instance:
        channel_layers = get_channel_layer()
        group_name = instance.doctor.username + "_{}".format(instance.doctor.id)
        print(group_name)
        log_message = ""
        
        status_dictionary = {
            1: "APPROVED",
            2: "DISAPPROVED",
            3: "PENDING"
        }
                    
        message = {
            'id': instance.id,
            'client': instance.client.id,
            'doctor': instance.doctor.id,
            'status': instance.status,
            'read': instance.read,
        }    
        
        if created:
            async_to_sync(channel_layers.group_send)(
                group_name,
                {
                    'type': 'appointments_listener',
                    'message': message,
                    'doc_id': instance.doctor.id,
                }
            )
            
            log_message = "New appointment created"
            print("SIGNAL_FEEDBACK:", log_message)   
        
        if instance._original_status != instance.status:
            print("ORIGINAL_STATUS: ", status_dictionary[instance._original_status])
            log_message = "Appointment status updated to: {}".format(instance.get_status())
            
            async_to_sync(channel_layers.group_send)(
                group_name,
                {
                    'type': 'doctor_response',
                    'message': message,
                    'doc_id': instance.doctor.id,
                }
            )
            
            print("SIGNAL_FEEDBACK:", log_message)
            


