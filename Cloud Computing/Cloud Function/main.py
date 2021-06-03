from google.cloud import storage
from google.api_core.client_options import ClientOptions
from googleapiclient import discovery
#from flask import jsonify
import numpy as np
import librosa
import tensorflow as tf
import json

def hello_gcs(event, context):
    file = event
    if '.wav' in file['name']:
        #print(f"Processing file: {file['name']}.")
        def preprocessing(audio_file, mode):
            # we want to resample audio to 16 kHz
            sr_new = 16000  # 16kHz sample rate
            x, sr = librosa.load(audio_file, sr=sr_new)
            max_len = 5 * sr_new  # length of sound array = time x sample rate
            if x.shape[0] < max_len:
                # padding with zero
                pad_width = max_len - x.shape[0]
                x = np.pad(x, (0, pad_width))
            elif x.shape[0] > max_len:
                # truncated
                x = x[:max_len]

            if mode == 'mfcc':
                feature = librosa.feature.mfcc(x, sr=sr_new)

            elif mode == 'log_mel':
                feature = librosa.feature.melspectrogram(y=x, sr=sr_new, n_mels=128, fmax=8000)
                feature = librosa.power_to_db(feature, ref=np.max)

            return feature

        def predict_json(project, region, model, instances, version=None):
            prefix = "{}-ml".format(region) if region else "ml"
            api_endpoint = "https://{}.googleapis.com".format(prefix)
            client_options = ClientOptions(api_endpoint=api_endpoint)
            service = discovery.build(
                'ml', 'v1', client_options=client_options)
            name = 'projects/{}/models/{}'.format(project, model)

            if version is not None:
                name += '/versions/{}'.format(version)

            response = service.projects().predict(
                name=name,
                body={'instances': instances}
            ).execute()

            if 'error' in response:
                raise RuntimeError(response['error'])

            return response['predictions']

        def download(filename):
            download_client = storage.Client()
            bucket = download_client.get_bucket('lunion-storage')
            blob = bucket.blob(filename)
            audio_file = '/tmp/'+filename
            blob.download_to_filename(audio_file)
            return audio_file

        print('mulai download')
        audio_file = download(file['name'])
        print('apakah download berhasil?')
        # preprocessing sound
        data = preprocessing(audio_file, mode='mfcc')
        data = np.array(data)
        data = data.reshape((20, 157, 1))
        data = np.expand_dims(data, axis=0)

        data = np.vstack([data])
        print('preprocess berhasil?')
        #model_path = 'saved_model/my_model'
        #new_model = tf.keras.models.load_model(model_path)

        # Predict sound data
        project = 'midyear-mason-312516'
        region = 'asia-southeast1'
        model = 'testlunion'
        version = 'v2'
        instances = data.tolist()
        response = predict_json(project, region, model, instances, version)
        classes = response[0]
        idx = np.argmax(classes)
        print('apakah prediksi ML berhasil?')

        c_names = ['Chronic Disease', 'Healthy', 'Non-Chronic Disease']
        prediction = c_names[idx]
        confidence_percentage = max(classes) * 100
        print('Lunion prediction: \n{}'.format(prediction))
        print('Confidence Percentage: {:.2f} %'.format(confidence_percentage))
        response_json = {"Prediction": prediction,"Confidence":confidence_percentage}
        print('apakah print berhasil?')

        def convert(request):
            result = request
            return json.dumps(result), 200, {'Content-Type': 'application/json'}
        
        #result = convert(response_json)
        upload_client = storage.Client()
        bucket = upload_client.get_bucket('lunion-storage')
        #blob = bucket.blob('{}.json'.format(file['name'][:-4]))
        #blobs = bucket.list_blobs(prefix='hasil.json')
        #for blob in blobs:
        #    blob.delete()
        #print('apakah delete berhasil?')
        
        blob = bucket.blob('hasil.json')
        
        blob.upload_from_string(
            data=json.dumps(response_json),
            content_type='application/json'
            )
            
        CACHE_CONTROL = "public, max-age=1"
        blob.cache_control = CACHE_CONTROL
        blob.patch()
        print('apakah set cache berhasil?')   
        #blob.upload_from_string(result)
        print('apakah upload berhasil?')
        #print(result)
