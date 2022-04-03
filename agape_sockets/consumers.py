import json 

from staff.models import *
from channels import Group
from channels.auth import channel_session_user, channel_session_user_from_http

@channel_session_user_from_http
def ws_connect(message):
    Group('users').add(message.reply_channel)
    Group('users').send({
        'text':json.dumps({
            'username':message.user.username,
            'is_logged_in':True
        })
    })
    
    
def ws_disconnect(message):
    Group('users').send({
        'text':json.dumps({
            'username':message.user.username,
            'is_logged_in':False
        })
    })
    Group('users').discard(message.reply_channel)