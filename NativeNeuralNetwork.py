
# REQUIREMENT: Install Numpy

import numpy as np
from PIL import Image
import os

from PythonTestCode import activationFunction


class Layer:
    def __init__(self, input_size, output_size):
        self.input_size = input_size
        self.output_size = output_size
        self.weights = self.init_weights()

    def __str__(self):
        return (f"Input size: {self.input_size}\nOutput size: {self.output_size}\n"
                f"Weights shape: {self.weights.shape}\n"
                f"Sample weights: {self.weights.flatten()[:5]}...")

    def init_weights(self):
            # Weights are initialized randomly with a small random number
            return np.random.randn(self.input_size, self.output_size) * 0.01

class NeuralNetwork:
    def __init__(self, input, hidden, output):
            self.input = input
            self.hidden = hidden
            self.output = output
    def __str__(self):
        return f"Input Layer:\n{self.input}\n\nHidden Layer:\n{self.hidden}\n\nOutput Layer:\n{self.output} "


    def populateWithData(self, data):
        pass
        # Ensure that the data has the correct shape to match the input layer

        # if data.shape != (self.input.columns, self.input.rows):
        #    raise ValueError(f"Input data shape {data.shape} does not match input layer shape "
        #                     f"({self.input.rows}, {self.input.columns})")

        # Only works for 1 image right now i think
        # self.input.initialized_data[0] = data


    def forwardPass(self, data):
        pass
        #self.populateWithData(data)

        #weightedSum = data(entries) * input.dot(weights)
        #activationFunction(weightedSum)

    def train(self, data):
        self.forwardPass(data)

        # Prints the initialized data
        print("Data inserted into input layer:")
        print(self.input.initialized_data)

#### GETTING THE DATA

# Get the directory for the file we are currently in
dirname = os.path.dirname(__file__)

# Get path for a given image inr oot
image_path = os.path.join(dirname, 'TestImage.jpg')

# Open the image and convert it to grayscale (L mode is greyscale mode)
img = Image.open(image_path).convert('L')

# Convert to a NumPy array and flatten it
numpyData = np.array(img)

# Divide every value within the numpyData array with 255.0 to normalize it
normalized_data = numpyData / 255.0


# Ensure full array is printed
np.set_printoptions(threshold=np.inf)

flattenedData = normalized_data.flatten(order='C').reshape(1, -1)



# Print the flattened array
print("Flattened data: ", flattenedData.shape)

##### NEURAL NETWORK STUFF

# Layer needs to take the width of a matrix and the height of a matrix
# Imaginary situation with 28 * 28 images
# This creates a vector with 784 columns and 1 row

# Use activation function to mark hidden layer
# Use loss function to mark output layer

input = Layer(28*28)

# Hidden layer is imagined with 50 neurons
hidden = Layer(50,100)

hidden = Layer(100,100)
hidden = Layer(100,100)
hidden = Layer(100,100)

output = Layer(100, 10)

nn = NeuralNetwork(input,hidden,output)
# nn.train(flattenedData)

# If a layer only has one argument. Automatically set column size to 1.
# inputOneArg = Layer(6)
# print(str(input) + "\n")

#print(nn)
