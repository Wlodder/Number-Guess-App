import numpy
import pandas as pd
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import Flatten
import csv
import h5py

#my_train_data = np.genfromtxt('mnist_train.csv',delimiter=',')
my_test_data = numpy.genfromtxt('mnist_test.csv',delimiter=',')

# commented out due to memory limits
# therefore only training on the test set of 10,000 labelled images
#x_train = my_train_data[1:,1:1000]
#y_train = my_train_data[0,1:1000]
x_test = my_test_data[:,1:]
y_test = my_test_data[:,0]

# converting the labels of the data to
# one hot encoded vectors
boundary = y_test.shape[0]
hot_array = numpy.zeros((boundary, 10))
for i in range(0,boundary):
    hot_array[i,int(y_test[i])] = 1

# building the model and adding the layers
model = Sequential()
model.add(Dense(units=50, activation='relu'))
model.add(Dense(units=50, activation='relu'))
model.add(Dense(units=10,activation='softmax'))
model.compile(loss='categorical_crossentropy',optimizer='sgd',metrics=['accuracy'])
model.fit(x_test,hot_array, epochs=75, batch_size=10000)
#loss_and_metrics = model.evaluate(x_test,hot_array, batch_size=10000)
# save the model to import later
model.save('C:\\Users\\wolod\\Documents\\Programming\\Java\\Neural network\\my_model.h5')

# extractingt the weights of the model and moving to a csv file
# to be imported into a java based model (feedforward only)
weights = model.get_weights()
with open('weights.csv','w') as writeFile:
    writer = csv.writer(writeFile)
    for weight in weights:
        if str(type(weight[0])) == "<class 'numpy.float32'>":
            writer.writerow(weight)
        else:
            for row in weight:
                writer.writerow(row)
