# 🚀 Lunion

[![Open In Colab](https://colab.research.google.com/assets/colab-badge.svg)](https://colab.research.google.com/github/DaffRazan/Lunion/blob/master/Machine%20Learning/Deep%20Learning%20pendeteksi%20penyakit%20paru.ipynb)
Lung disease is one of the highest causes of death worldwide. Simple early detection can
reduce death rate by hearing the sound of a patient's lung using a stethoscope. However, the
access to detect this disease is still lacking and the results of the doctor's diagnosis are prone to
errors. To tackle these problems, we build a mobile application that detects lung diseases based
on the respiratory sound of patients.

# 📌 Usage 📌

## 🤖 Machine Learning (Colab Notebook)

* Download respiratory dataset from https://www.kaggle.com/vbookshelf/respiratory-sound-database
* Create dataframe that contain filename and its label
* Load and resample sound data to be 16 kHz sound using librosa library
* Truncate or pad sound data to be 5s from beginning of sound so all of the data have same dimension.
* Extract MFCC features from sound data
* Build, train, and test deep learning model using tensorflow. You can use transfer learning from tensorflow hub or use our model architecture on our github. https://github.com/DaffRazan/Lunion
* Save model as saved model format or MDF5
* Create prediction python script. 

## ☁️ Cloud Computing (Google Cloud Storage, AI Platform, Flask Web Service)

* Create a bucket in Google Cloud Storage with standard default storage class, choosing the closest region asia-southeast2(Jakarta), uniform access control and add allUsers with Cloud Storage Admin.
* Create new model in AI Platform choosing the closest region asia-southeast2(Jakarta) and create new version inside it by specifying the pre-built container settings accordingly to Machine Learning model that has been tested locally and choose the n1-standard-2 machine type for cheaper deployment.
* Create a Cloud Function that is triggered by Cloud Storage with Finalize/Create event type and select the target bucket that has been created before, then migrate the python script to run the preprocessing and calling the model from AI Platform into the Cloud Function, finally upload the prediction result into the bucket as json format file. Fill the requirements.txt for each version of the library used.
* Create a web service using Flask to upload the file from user to the bucket then return the public URL of the predicted result from the bucket to user and deploy it to App Engine so that it can be served on mobile.

## 📱 Mobile Development (Mobile App)

* Download and install latest version of Android Studio
* Clone mobile app project on github https://github.com/DaffRazan/Lunion 
* Open Android Studio and open folder app project named LunionApp
* Connect App to Firebase
* Add the Firebase Realtime Database to the app
* Configure Realtime Database Rules
* Write database query
* Read database
* Plug in real device with USB or connect via bluetooth to PC
* Run the application with the real device or emulator via AVD Manager
