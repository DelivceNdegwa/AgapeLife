#! /usr/bin/python
# ! -*- coding: utf-8 -*-

import sys
import os
import time
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from RtmTokenBuilder import RtmTokenBuilder,Role_Rtm_User

appID = "0ceac686d87c4ba9811edbc7ee666356"
appCertificate = "e25d627fbcf04ad183a8b528aa216acc"
user = "test_user_id"
expirationTimeInSeconds = 3600
currentTimestamp = int(time.time())
privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds

def main():
    token = RtmTokenBuilder.buildToken(appID, appCertificate, user, Role_Rtm_User, privilegeExpiredTs)
    print("Rtm Token: {}".format(token))


if __name__ == "__main__":
    main()
