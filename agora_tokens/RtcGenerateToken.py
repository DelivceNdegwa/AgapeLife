#! /usr/bin/python
# ! -*- coding: utf-8 -*-

import sys
import os
import time
from random import randint


from agora_token_builder import RtcTokenBuilder

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

#while the RTM channel transmits messaging or signaling data.
class TokenGenerator:
    def __init__(self, uid, channelName, startTime, expiryTimeInSeconds, userRole):
        self.uid = uid
        self.channelName = channelName
        self.__appID="f320179c8f6947c29eb4a6e48f2a0cad"
        self.__appCertificate = "a438e6def2f4466c931b39b755947927"
        self.userAccount = uid
        self.expireTimeInSeconds = expiryTimeInSeconds
        self.startTime = startTime
        self.userRole = userRole
        # self.privilegeExpiredTs = self.startTime + self.expireTimeInSeconds
        
    def to_integer(self, dt_time):
        return 10000*dt_time.year + 100*dt_time.month + dt_time.day
    
    
    def validate_channel_name(self):
        if " " in self.channelName:
            self.channelName = self.channelName.replace(" ", "_")
        
        self.channelName = self.channelName.lower()
        
        return self.channelName
       
        
    def depreciated_generate(self):
        from .RtcTokenBuilder import RtcTokenBuilder,Role_Attendee
        # token = RtcTokenBuilder.buildTokenWithUid(self.__appID, self.__appCertificate, self.channelName, self.uid, Role_Attendee, self.privilegeExpiredTs)
        # print("Token with int uid: {}".format(token))
        self.privilegeExpiredTs = self.to_integer(self.startTime) + self.expireTimeInSeconds
        token = RtcTokenBuilder.buildTokenWithAccount(self.__appID, self.__appCertificate, self.validate_channel_name(), self.userAccount, Role_Attendee, self.privilegeExpiredTs)
        # print("Token with user account: {}".format(token))  
        
        return token  
    
    def generate(self):
        self.privilegeExpiredTs = self.to_integer(self.startTime) + self.expireTimeInSeconds
        token = RtcTokenBuilder.buildTokenWithAccount(self.__appID, self.__appCertificate, self.validate_channel_name(), self.userAccount, self.userRole, self.privilegeExpiredTs)
# currentTimestamp = int(time.time())


