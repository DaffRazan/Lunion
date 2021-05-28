# Here's a codeblock just for fun. You should be able to upload an sound here
# and have it classified without crashing

import numpy as np
import librosa
import tensorflow as tf
import json

def preprocessing(audio_file, mode):
    # we want to resample audio to 16 kHz
    sr_new = 16000  # 16kHz sample rate
    x, sr = librosa.load(audio_file, sr=sr_new)

    # padding sound
    # because duration of sound is dominantly 20 s and all of sample rate is 22050
    # we want to pad or truncated sound which is below or above 20 s respectively
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

# load model
# Recreate the exact same model, including its weights and the optimizer
model_path = 'saved_model/my_model'
new_model = tf.keras.models.load_model(model_path)



audio_file = '101_1b1_Al_sc_Meditron.wav'

# preprocessing sound
data = preprocessing(audio_file, mode='mfcc')
data = np.array(data)
data = data.reshape((20, 157, 1))
data = np.expand_dims(data, axis=0)

datas = np.vstack([data])

# Predict sound data
classes = new_model.predict(datas, batch_size=10)
idx = np.argmax(classes)

c_names = ['Chronic Disease', 'Healthy', 'Non-Chronic Disease']
print('Lunion prediction: \n{}'.format(c_names[idx]))
print('Confidence Percentage: {:.2f} %'.format(np.max(classes) * 100))


file = open("1.txt", "w")
file.write("Lunion Prediction : {}".format(c_names[idx]))
file.write('Confidence Percentage: {:.2f} %'.format(np.max(classes) * 100))
data = json.dump(data.tolist(), file)
file.close()


print(data)