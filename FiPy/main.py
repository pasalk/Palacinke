import pycom
import time

pycom.heartbeat(False)

while True:
    pycom.rgbled(0x0000FF)  # Blue
    time.sleep(1)