from RtcGenerateToken import TokenGenerator
import time

token_generator = TokenGenerator(2546, "delivce-channel", int(time.time()), 3600)

print(token_generator.generate())