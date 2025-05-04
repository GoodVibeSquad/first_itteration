
# REQUIREMENT: Install Numpy

import numpy as np
from PIL import Image
import os

from PythonTestCode import activationFunction


class Layer:
    def __init__(self, *args):
        if len(args) == 1:
            self.rows = args[0]
            self.columns = 1

        if len(args) == 2:
            self.rows = args[0]
            self.columns = args[1]

        # Initialize weights for a layer
        # Might be a bit inefficient right now because input layers
        # Also have weights
        self.weights = self.init_weights()

        # Initialized data for input layer
        # This vector is treated as a row vector if it's only 1 image(Only 1 row)
        # Or a matrix if there are multiple images inserted
        self.initialized_data = np.zeros((self.rows, self.columns))

        # Neuron output data that is output after an activation function
        # has been run on a given weighted sum.
        # self.output_data = np.zeros((self.columns, self.rows))

    def __str__(self):
        return (f"Rows: {self.rows}\nColumns: {self.columns}\n"
                f"Weights Shape: {self.weights.shape}\n"
                f"Sample Weights: {self.weights.flatten()[:5]}...")  # Show first 5 weights

    def init_weights(self):
            # Weights are initialized randomly with a small random number
            return np.random.randn(self.rows, self.columns) * 0.01




class NeuralNetwork:
    def __init__(self, input, hidden, output):
            self.input = input
            self.hidden = hidden
            self.output = output
    def __str__(self):
        return f"Input Layer:\n{self.input}\n\nHidden Layer:\n{self.hidden}\n\nOutput Layer:\n{self.output} "


    def populateWithData(self, data):
        # Ensure that the data has the correct shape to match the input layer

        if data.shape != (self.input.columns, self.input.rows):
            raise ValueError(f"Input data shape {data.shape} does not match input layer shape "
                             f"({self.input.rows}, {self.input.columns})")

        # Only works for 1 image right now i think
        self.input.initialized_data[0] = data


    def forwardPass(self, data):
        self.populateWithData(data)

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

flattenedData = normalized_data.flatten().reshape(1, -1)

# Print the flattened array
# print(flattenedData)



##### NEURAL NETWORK STUFF

# Layer needs to take the width of a matrix and the height of a matrix
# Imaginary situation with 28 * 28 images
# This creates a vector with 784 columns and 1 row
input = Layer(28*28)


# Hidden layer is imagined with 50 neurons
hidden = Layer(50,6)
output = Layer(10, 6)

nn = NeuralNetwork(input,hidden,output)
nn.train(flattenedData)

# If a layer only has one argument. Automatically set column size to 1.
# inputOneArg = Layer(6)
# print(str(input) + "\n")

#print(nn)
