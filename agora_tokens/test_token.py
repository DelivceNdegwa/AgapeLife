from RtcGenerateToken import TokenGenerator
import time

token_generator = TokenGenerator("brian_12", "AgapeTest", int(time.time()), 85400)

print(token_generator.generate())