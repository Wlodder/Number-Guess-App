# Number-Guess-App
Android app that guesses number drawn

For the most updated product see directory named "itstesttime". Its called this because its my first venture into android development
and I didn't think I would finish or develop the project. Other directories are for testing or are datasets and auxiliary resources.

The aim of the project was to use machine learning in a mobile setting. The machine learning used was the Google tensorflow framework
with models from keras, all designed and tested by hand. The model was trained using the MNIST dataset from of over 60,000 labelled 
handwritten digits for the training set and 10,000 labelled images for the test set. A subset of 5,000 from the test set
was used for cross validation. 3 variations of the same network were tested with speed of training being the priority. 
A pip installed 4 layer neural network (keras), 4 layer neural network using conda(keras) and a java neural network (of same
design) from scratch. The order of the speed of training: conda, java, pip. With the conda and java roughly taking 5 minutes with 75 epochs
and pip taking >30 minutes for the test set alone. In the end conda was chosen because it is less bug prone than java.
Tensorflow-lite-1.14.0 was used to access the tensorflow model on the android.

The android app was made in 100% java using 1 external resource the ambilwarna dialog color picker, all else was done from scratch.
gradle requirements:
min sdk version - 22
target sdk version - 28
compile sdk version - 28

tested on : LeMobile Le X829 (physical), Pixel 2 API 28 (virtual) , Pixel API 28 (virtual)

sources - ambil warna : https://github.com/yukuku/ambilwarna
        - MNIST data set - http://yann.lecun.com/exdb/mnist/
