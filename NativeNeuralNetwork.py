# REQUIREMENT: Install Numpy

import numpy as np
from PIL import Image
import os


#Temp activation function
def activationFunction(x, name='none'):
    return x  # Just returns input unchanged for now

class Layer:
    def __init__(self, *args):

        # For input layer
        # Layer(Input data size)
        if len(args) == 1:
            self.input_size = args[0]
            self.output_size = 0

        # For multiple hidden layers
        # 5 Hidden layers each of size 2
        # Layer(5,2, relu)
        elif len(args) == 3:
            self.hidden_layers_amount = args[0]
            self.hidden_layers_size = args[1]
            self.activation_function = "Temp"

        # For output layers
        elif len(args) == 4:
            self.input_size = args[0]
            self.output_size = args[1]
            self.activation_function = "Temp"
            self.loss_function = "Temp"

        self.output = None  # Will store output for each layer

    def __str__(self):
        weight_shape = self.weights.shape if self.weights is not None else "None"
        return f"Input size: {self.input_size}, Output size: {self.output_size}, Weights: {weight_shape}"

    def init_weights(self):
        pass

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
        self.input.output_size = self.hidden.hidden_layers_size

        # Set output of input to be the same as hidden layer
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
# We imagine the images to be 28px * 28px images such as Mnist dataset
# This creates a vector with 784 columns and 1 row

# The input layer contains the data
# The output is automatically matched with the neuron size of the hidden layers
input = Layer(28*28)

# 50 Rows and 5 columns basically
# 50 Neurons on each row
# 5 Hidden layers
# Activation function is a given activation function such as Relu
hidden = Layer(50, 5, "Activation function")

# 10 Classifications (0-9) Output size is 10
# Activation function is a given activation function such as Relu
output = Layer(10, "Activation Function")

nn = NeuralNetwork(input,hidden,output)

nn.train(flattenedData)
print(nn.input.initialized_input)