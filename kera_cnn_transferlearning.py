from __future__ import print_function
from keras.applications import VGG16
from keras import models
from keras.models import Model
import matplotlib.pyplot as plt
from keras.utils import to_categorical
from keras.models import load_model
import os
from keras import layers
from keras import optimizers
from keras.preprocessing.image import ImageDataGenerator, load_img
import keras
import csv
import matplotlib.pyplot as plt
import numpy as np
import os, shutil

vgg_conv = VGG16(weights='imagenet',
                  include_top=False,
                  input_shape=(224, 224, 3))

folder = 'classify/'
validation_dir = 'classify/'
datagen = ImageDataGenerator(rescale=1./255)
batch_size = 20

for the_file in os.listdir(folder):
    file_path = os.path.join(folder, the_file)
    try:
        if os.path.isfile(file_path):
            os.unlink(file_path)
        #elif os.path.isdir(file_path): shutil.rmtree(file_path)
    except Exception as e:
        print(e)

your_list = []
with open('actions_100219_output.txt') as f:
    your_list = f.read().splitlines()

count = 0
nVal = 1
for item in your_list:
    if '[' in item:
        subItem = item[2:-1]
        values = subItem.split(',')
        #print(values)
        plyr1 = []
        plyr1Colors = []
        plyr1InitialColor = [0, 0, 0]
        for num in range(0, int((len(values)/9))):
            firstPos = (num*9)
            x = values[int(firstPos)]
            y = values[int(firstPos + 1)]
            plyr1.append([float(x), float(y)])
            currentColor = plyr1InitialColor.copy()
            currentColor[2] = currentColor[2] + 0.05
            if currentColor[2] > 1:
                currentColor[2] = 1
            plyr1InitialColor = currentColor
            plyr1Colors.append(currentColor)
        #print(plyr1Colors)
        colA = [row[0] for row in plyr1]
        colB = [row[1] for row in plyr1]
        plt.scatter(colA, colB, color= plyr1Colors )
        plt.plot(colA, colB)

        plyr2 = []
        plyr2Colors = []
        plyr2InitialColor = [1, 0, 0]
        for num in range(0, int(len(values)/ 9)):
            firstPos = (num*9)
            x = values[int(firstPos+3)]
            y = values[int(firstPos+4)]
            plyr2.append([float(x) , float(y)])
            currentColor = plyr2InitialColor.copy()
            currentColor[2] = currentColor[2] + 0.05
            if currentColor[2] > 1:
                currentColor[2] = 1
            plyr2InitialColor = currentColor
            plyr2Colors.append(currentColor)
        colA = [row[0] for row in plyr2]
        colB = [row[1] for row in plyr2]
        plt.scatter(colA, colB, color= plyr2Colors )
        plt.plot(colA, colB)

        ball = []
        ballColors = []
        ballInitialColor = [0, 1, 0]
        for num in range(0, int(len(values)/9)):
            firstPos = (num*9)
            x = values[int(firstPos+6)]
            y = values[int(firstPos+7)]
            ball.append([float(x) , float(y)])
            currentColor = ballInitialColor.copy()
            currentColor[0] = currentColor[0] + 0.05
            if currentColor[0] > 1:
                currentColor[0] = 1
            ballInitialColor = currentColor
            ballColors.append(currentColor)
        colA = [row[0] for row in ball]
        colB = [row[1] for row in ball]
        plt.scatter(colA, colB, color = ballColors)
        plt.plot(colA, colB)
        plt.savefig('classify/pass/' + your_list[count-1] + '.png')
        plt.clf()


    count = count + 1
model = load_model('cnn_first_model_080219.h5')
validation_features = np.zeros(shape=(nVal, 7, 7, 512))
validation_labels = np.zeros(shape=(nVal,4))

validation_generator = datagen.flow_from_directory(
    validation_dir,
    target_size=(224, 224),
    batch_size=batch_size,
    class_mode='categorical',
    shuffle=False)
i = 0
for inputs_batch, labels_batch in validation_generator:
    features_batch = vgg_conv.predict(inputs_batch)
    validation_features[i * batch_size : (i + 1) * batch_size] = features_batch
    validation_labels[i * batch_size : (i + 1) * batch_size] = labels_batch
    i += 1
    if i * batch_size >= nVal:
        break
 
validation_features = np.reshape(validation_features, (nVal, 7 * 7 * 512))

fnames = validation_generator.filenames
ground_truth = validation_generator.classes
 
label2index = validation_generator.class_indices
 
# # Getting the mapping from class index to class label
dx2label = dict((v,k) for k,v in label2index.items())
predictions = model.predict_classes(validation_features)
prob = model.predict(validation_features)

#for i in range(len(predictions)):
#print(str(predictions[0]) + "," + str(prob[0]), file=open("../cnn_output.txt", "w"))
print(str(predictions[0]) + "," + str(prob[0]))
#print(prob)

