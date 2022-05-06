#! /usr/bin/python
# ! -*- coding: utf-8 -*-

import sys
import os
import time
from random import randint
from .RtcTokenBuilder import RtcTokenBuilder,Role_Attendee

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

#while the RTM channel transmits messaging or signaling data.
class TokenGenerator:
    def __init__(self, uid, channelName, startTime, expiryTimeInSeconds):
        self.uid = uid
        self.channelName = channelName
        self.__appID="0ceac686d87c4ba9811edbc7ee666356"
        self.__appCertificate = "e25d627fbcf04ad183a8b528aa216acc"
        self.userAccount = uid
        self.expireTimeInSeconds = expiryTimeInSeconds
        self.startTime = startTime
        # self.privilegeExpiredTs = self.startTime + self.expireTimeInSeconds
        
    def to_integer(self, dt_time):
        return 10000*dt_time.year + 100*dt_time.month + dt_time.day
    
    
    def validate_channel_name(self):
        if " " in self.channelName:
            self.channelName = self.channelName.replace(" ", "_")
        
        self.channelName = self.channelName.lower()
        
        return self.channelName
       
        
    def generate(self):
        # token = RtcTokenBuilder.buildTokenWithUid(self.__appID, self.__appCertificate, self.channelName, self.uid, Role_Attendee, self.privilegeExpiredTs)
        # print("Token with int uid: {}".format(token))
        self.privilegeExpiredTs = self.to_integer(self.startTime) + self.expireTimeInSeconds
        token = RtcTokenBuilder.buildTokenWithAccount(self.__appID, self.__appCertificate, self.validate_channel_name(), self.userAccount, Role_Attendee, self.privilegeExpiredTs)
        # print("Token with user account: {}".format(token))  
        
        return token  
    

# currentTimestamp = int(time.time())


