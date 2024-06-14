from flask import Flask, request, jsonify

# Kreira HTTP server da može primati zahtjeve

app = Flask(__name__)


@app.route('/api/webhook/humidity_data', methods=['POST'])
def receive_humidity_data():
    data = request.json
    if data is not None and 'humidity' in data and 'device' in data:
        humidity = data['humidity']
        device_id = data['device']
        print(f"Received humidity data: {humidity} from device {device_id}")
        print("Received data:")
        for key, value in data.items():
            print(f"  {key}: {value}")
        return jsonify({'status': 'success'}), 200
    else:
        return jsonify({'error': 'Missing humidity or device data'}), 400

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8123)
