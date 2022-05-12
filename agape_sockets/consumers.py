import json
from channels.generic.websocket import WebsocketConsumer
from asgiref.sync import async_to_sync

class TestModelConsumer(WebsocketConsumer):
    def connect(self):
        self.room_group_name='test_group_doctors_online'
        
        async_to_sync(self.channel_layer.group_add)(
            self.room_group_name,
            self.channel_name
        )
        
        self.accept()
        
    def receive(self, text_data):
        text_data_json = json.loads(text_data)
        message = text_data_json["message"]
        
        async_to_sync(self.channel_layer.group_send)(
            self.room_group_name,
            {
                "type":"sent_message", #Name of function to handle the event
                "message":message
            }
        )
        
    def sent_message(self, event):
        message = event["message"]
        self.send(json.dumps({
            'type':'model_broadcast',
            'message':message
        }))
        