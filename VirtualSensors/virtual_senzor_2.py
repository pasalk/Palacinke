import requests
import json
import random
import time
import socket
import _thread
import threading
from colorama import init, Fore, Style

# Initialization for Colorama
init()

# HA_URL = 'http://127.0.0.1:8123/api/webhook/humidity_data'
HA_URL = 'http://192.168.184.133:8123/api/webhook/device_2_humidity_data' 
TOKEN = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiI0ZDc5NjVkZTliYmY0NzJjOTRlOWJjNzE4ZTExNWEyYyIsImlhdCI6MTcxNzY3MTgwMSwiZXhwIjoyMDMzMDMxODAxfQ.WPnw8YSPLFNJKEkMCaLcfHtUFVz6i_chkb36aCTBkUY'


#===================================================================#
#                         HTTP client
#===================================================================#

def fetch_humidity_data():
    humidity = random.gauss(65, 3)
    humidity = round(humidity)
    humidity = max(0, min(100, humidity))
    return humidity


def send_humidity_data():
    while True:
        humidity = fetch_humidity_data()
        headers = {
            'Authorization': f'Bearer {TOKEN}',
            'Content-Type': 'application/json'
        }
        payload = {
            'humidity': humidity,
            'device': '2'
        }

        try:
            response = requests.post(HA_URL, data=json.dumps(payload), headers=headers)
            if response.status_code == 200:
                print("2 Data sent successfully")
                print("Response from server:", response.json())
            else:
                print(f"Failed to send data: {response.status_code} - {response.text}")
            response.close()
        except requests.exceptions.RequestException as e:
            #print(f"An error occurred: {e}")
            pass
        time.sleep(10)



#===================================================================#
#                         HTTP Server
#===================================================================#

def control_led(color):
    if color == "nowater":
        print(Fore.YELLOW + "LED color set to " + Fore.GREEN + "green" + Style.RESET_ALL)
    elif color == "water":
        print(Fore.YELLOW + "LED color set to " + Fore.BLUE + "blue" + Style.RESET_ALL)


def handle_request(conn):
    try:
        request = conn.recv(2048).decode('utf-8')
        print(Fore.YELLOW + 'Content = %s' % request)

        body_start = request.find('\r\n\r\n')
        if body_start != -1:
            body = request[body_start + 4:]
            # print('Body:', body)

            params = json.loads(body)
            print(Fore.YELLOW + 'Params = %s' % params)

            # Obrada JSON tijela poruke
            if body:
                params = json.loads(body)
                print(Fore.YELLOW + 'Parameters:', params)

                if 'color' in params:
                    if params['color'] == 'water':
                        print(Fore.YELLOW + "Setting the color to blue" + Style.RESET_ALL)
                        control_led(params['color'])
                    elif params['color'] == 'nowater':
                        print(Fore.YELLOW + "Setting the color to blue" + Style.RESET_ALL)
                        control_led(params['color'])

        response = 'HTTP/1.1 200 OK\nContent-Type: text/html\nConnection: close\n\nOK'
        conn.send(response.encode())
    except Exception as e:
        #print(Fore.RED + "Error handling request:", e)
        pass
    finally:
        conn.close()


def http_server():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(('', 81 ))
    s.listen(5)
    print(Fore.YELLOW + "HTTP Server is running on port 81..." + Style.RESET_ALL)

    while True:
        conn, addr = s.accept()
        print(Fore.YELLOW + 'Got a connection from' + Style.RESET_ALL, addr)
        _thread.start_new_thread(handle_request, (conn,))


#===================================================================#


if __name__ == '__main__':
    # Start the server thread
    server_thread = threading.Thread(target=http_server)
    server_thread.start()

    # Start the humidity data sending thread
    data_thread = threading.Thread(target=send_humidity_data)
    data_thread.start()

    # Wait for both threads to finish (they won't unless the application is killed)
    server_thread.join()
    data_thread.join()
