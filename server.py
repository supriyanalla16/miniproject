from flask import Flask, request, jsonify
from flask_cors import CORS
import requests
import json
import firebase_admin
from firebase_admin import credentials, firestore

app = Flask(__name__)
CORS(app, resources={r"/send_history": {"origins": "*"}})

# Initialize Firebase Admin SDK
cred = credentials.Certificate(r"C:\Users\Supriya Nalla\Downloads\login-register-firebase-895ce-firebase-adminsdk-xppm6-a6aaa77ad1.json")
firebase_admin.initialize_app(cred)

db = firestore.client()

@app.route('/send_history', methods=['POST'])
def receive_history():
    data = request.json
    print("Received data:", data)  # Log the received data
    user_uid = data.get('uid')  # Fetch UID from request data
    print("Received UID:", user_uid)  # Log the UID

    if not user_uid:
        return jsonify({'message': 'UID not provided'}), 400

    user_data = fetch_fcm_token(user_uid)
    if user_data:
        # Send notification using FCM
        send_fcm_notification(user_data['fcmToken'], data)
        return jsonify({'message': 'Data received and notification sent successfully'})
    else:
        return jsonify({'message': 'User data not found'}), 404

def fetch_fcm_token(user_uid):
    try:
        user_doc = db.collection('tokens').document(user_uid).get()
        if user_doc.exists():
            return user_doc.to_dict()
        else:
            print(f'No document found for user UID: {user_uid}')
            return None
    except Exception as e:
        print(f'Error fetching user data: {e}')
        return None

def send_fcm_notification(fcm_token, data):
    FCM_SERVER_KEY = '73a64d0bd816db326867b9f9ca065ac161dc4003'
    FCM_API_URL = 'https://login-register-firebase-895ce-default-rtdb.firebaseio.com'
    
    headers = {
        'Content-Type': 'application/json',
        'Authorization': f'key={FCM_SERVER_KEY}'
    }
    
    payload = {
        'to': fcm_token,  # Use the fetched FCM token
        'notification': {
            'title': 'Alert: Negative Content Detected',
            'body': f"You visited a page with sensitive content: {data.get('title', data['url'])}"
        }
    }
    
    response = requests.post(FCM_API_URL, headers=headers, json=payload)
    
    try:
        response.raise_for_status()  # Raise an HTTPError for bad responses (4xx or 5xx)
        response_data = response.json()
        print('Notification sent:', response_data)
    except requests.exceptions.HTTPError as http_err:
        print(f'HTTP error occurred: {http_err}')
        print(f'Response text: {response.text}')
    except requests.exceptions.RequestException as req_err:
        print(f'Request error occurred: {req_err}')
    except json.JSONDecodeError as json_err:
        print(f'JSON decode error: {json_err}')
        print(f'Response text: {response.text}')

if __name__ == '__main__':
    app.run(debug=True, port=5000)