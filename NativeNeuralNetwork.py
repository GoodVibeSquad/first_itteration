# REQUIREMENT: Install Numpy

import numpy as np
from PIL import Image
import os

from PythonTestCode import activationFunction


class Layer:
    def __init__(self, *args):
        if len(args) == 1:
            self.input_size = args[0]
            self.output_size = None
            self.weights = None
        elif len(args) == 2:
            self.input_size = args[0]
            self.output_size = args[1]
            self.weights = self.init_weights()
        else:
            raise ValueError("Layer must take 1 (input) or 2 (input, output) sizes.")

        self.output = None  # Will store activations or data
        self.weighted_sum = None  # For the pre-activation values

    def __str__(self):
        weight_shape = self.weights.shape if self.weights is not None else "None"
        return f"Input size: {self.input_size}, Output size: {self.output_size}, Weights: {weight_shape}"

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


    def initialize_input_data(self, data):
        self.input.initialized_input = data


    def forwardPass(self, data):
        self.initialize_input_data(data)

        current_output = self.input.output


        #weightedSum = data(entries) * input.dot(weights)
        #activationFunction(weightedSum)

    def train(self, data):
        self.forwardPass(data)


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
output = Layer(100, 10)

nn = NeuralNetwork(input,hidden,output)
nn.train(flattenedData)
print(nn.input.initialized_input)

# If a layer only has one argument. Automatically set column size to 1.
# inputOneArg = Layer(6)
# print(str(input) + "\n")

#print(nn)
