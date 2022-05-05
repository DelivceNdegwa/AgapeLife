#! /usr/bin/python
# ! -*- coding: utf-8 -*-

import sys
import os
import time
from random import randint
from RtcTokenBuilder import RtcTokenBuilder,Role_Attendee

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

#while the RTM channel transmits messaging or signaling data.

appID = "0ceac686d87c4ba9811edbc7ee666356"
appCertificate = "e25d627fbcf04ad183a8b528aa216acc"
channelName = "Test-Channel"
uid = 23
userAccount = "e25d627fbcf04ad183a8b528aa216acc"
expireTimeInSeconds = 3600
currentTimestamp = int(time.time())
privilegeExpiredTs = currentTimestamp + expireTimeInSeconds


def main():
    token = RtcTokenBuilder.buildTokenWithUid(appID, appCertificate, channelName, uid, Role_Attendee, privilegeExpiredTs)
    print("Token with int uid: {}".format(token))
    token = RtcTokenBuilder.buildTokenWithAccount(appID, appCertificate, channelName, userAccount, Role_Attendee, privilegeExpiredTs)
    print("Token with user account: {}".format(token))


if __name__ == "__main__":
    main()
