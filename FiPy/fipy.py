import time
import pycom
import machine
from machine import Pin
import socket
import ssl
from dth import DTH
import urequests
from network import WLAN
import _thread
import socket
import json

pycom.heartbeat(False)

#                         RGB LED Setup 
#===================================================================#

RED_PIN = Pin('P12', mode=Pin.OUT)
GREEN_PIN = Pin('P10', mode=Pin.OUT)
BLUE_PIN = Pin('P11', mode=Pin.OUT)

def set_rgb_color(r, g, b):
    RED_PIN.value(r)
    GREEN_PIN.value(g)
    BLUE_PIN.value(b)
    
#===================================================================#

#                     Connect to the Wi-Fi 
#===================================================================#

SSID = 'IOT_4'
PASSWORD = 'iot12345'

#SSID = 'A1_737810624'
#PASSWORD = '6ZEs2W0V'

wlan = WLAN(mode=WLAN.STA)

nets = wlan.scan()
for net in nets:
    print(net.ssid)
    if net.ssid == SSID:
        
        print('Network found!')
        wlan.connect(net.ssid, auth=(net.sec, PASSWORD), timeout=5000)
        while not wlan.isconnected():
            machine.idle() # save power while waiting
        print('WLAN connection succeeded!')
        break
time.sleep(5)
print(wlan.ifconfig())

#===================================================================#

#                         HTTP client
#===================================================================#

HA_URL = 'http://192.168.184.133:8123/api/webhook/device_1_humidity_data'
ACCESS_TOKEN = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiI0ZDc5NjVkZTliYmY0NzJjOTRlOWJjNzE4ZTExNWEyYyIsImlhdCI6MTcxNzY3MTgwMSwiZXhwIjoyMDMzMDMxODAxfQ.WPnw8YSPLFNJKEkMCaLcfHtUFVz6i_chkb36aCTBkUY'

def send_data_to_home_assistant(humidity):
    headers = {
        'Authorization': 'Bearer ' + ACCESS_TOKEN,
        'Content-Type': 'application/json'
    }
    payload = {
        'humidity': humidity,
        'device': '1'
    }
    #print(payload)
    json_payload = json.dumps(payload)
    #print(json_payload)
    try:
        response = urequests.post(HA_URL, data=json_payload, headers=headers)
        #print(response.status_code)
        #print(response.headers)
        #print(response.text)
        #response.close()
        print("Data sent successfully")
    except Exception as e:
        print("Failed to send data:", e)
#===================================================================#

#                         HTTP Server
#===================================================================#
def http_server():
    def control_water(command):
        if command == "nowater":
            pycom.rgbled(0x000f00)
            set_rgb_color(0, 1, 0)
        elif command == "water":
            pycom.rgbled(0x0000FF)
            set_rgb_color(0, 0, 1)

    def handle_request(conn):
        try:
            request = conn.recv(2048)  # Read the incoming request
            print('Raw request: %s' % request)
            request = request.decode('utf-8')
            print('Decoded request: %s' % request)

            # Look for the end of the headers (double CRLF)
            body_start = request.find('\r\n\r\n')
            if body_start != -1:
                # Split the headers and the body
                body = request[body_start+4:]  # Skip over the CRLFs
                headers = request[:body_start]
                print('Headers: %s' % headers)
                print('Body: %s' % body)

                params = json.loads(body)
                print('Params = %s' % params)

                # Attempt to parse the JSON body
                if body:
                    try:
                        params = json.loads(body)
                        print('Parsed params: %s' % params)

                        if 'color' in params:
                            if params['color'] == 'water':
                                print("Setting the color to blue")
                                control_water("water")
                            elif params['color'] == 'nowater':
                                print("Setting the color to green")
                                control_water("nowater")
                    except json.JSONDecodeError as e:
                        print("JSON decode error:", e)
                else:
                    print("No body received or body is empty")
            else:
                print("No body found in the request")

            # Send a response back to the client
            response = 'HTTP/1.1 200 OK\nContent-Type: text/html\nConnection: close\n\nOK'
            conn.send(response.encode('utf-8'))  # Ensure the response is encoded
        except Exception as e:
            print("Error handling request:", e)
        finally:
            conn.close()
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(('', 80))
    s.listen(5)

    while True:
        conn, addr = s.accept()
        print('Got a connection from %s' % str(addr))
        _thread.start_new_thread(handle_request, (conn,))

# Start the HTTP server in a separate thread
_thread.start_new_thread(http_server, ())

#===================================================================#


#                          Main loop
#===================================================================#

sensor = DTH(Pin('P3', mode=Pin.OPEN_DRAIN), 0)
time.sleep(1)
pycom.rgbled(0x000f00) #Set LED green
set_rgb_color(0, 1, 0)


while True:   
    #Read value
    result = sensor.read()
    while not result.is_valid():
        time.sleep(.5)
        result = sensor.read()

    #Print the values in the console:
    #print("Temperature: {}°C".format(result.temperature))
    print("Humidity: {}%".format(result.humidity))
        
    # Send data to Home Assistant
    send_data_to_home_assistant(result.humidity)

    time.sleep(10)

#===================================================================#