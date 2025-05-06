# REQUIREMENT: Install Numpy

import numpy as np
from PIL import Image
import os


# ACTIVATION FUNCTION INFO
class activationFunction:
    def run(x):
        return x

# Relu can use the amazingMethod!
class Relu(activationFunction):
    def run(x):
        return max(0,x)


class Layer:
    def __init__(self, *args):
        # For input layer
        # Layer(Input data size)
        if len(args) == 1:
            self.input_size = args[0]
            self.initialized_input = 0

        # For output layers
        # Loss function is found on the neural network itself
        # For output layer
        elif len(args) == 2:
            self.output_size = args[0]
            self.activation_function = args[1]

        # For multiple hidden layers
        elif len(args) == 3:
            self.hidden_layers_amount = args[0]
            self.hidden_layers_size = args[1]
            self.activation_function = args[2]


# ADDING LOSS FUNCTION TO NEURAL NETWORK
class NeuralNetwork:
    def __init__(self, input, hidden, output):
        self.input = input
        self.hidden = hidden
        self.output = output
        self.weights_array = self.init_weights()

    def initialize_input_data(self, data):
        self.input.initialized_input = data

    def init_weights(self):
        weights = []

        input_weights = np.random.rand(self.input.input_size, self.hidden.hidden_layers_size)
        print("Input weights: ", input_weights.shape)
        weights.append(input_weights)

        # LOOP FOR GENERATING WEIGHTS FOR HIDDEN LAYERS
        # Hidden layer to hidden layer connections
        for _ in range(self.hidden.hidden_layers_amount - 1):
            hidden_weights = np.random.rand(self.hidden.hidden_layers_size, self.hidden.hidden_layers_size)
            print("Hidden weight: ", hidden_weights.shape)
            weights.append(hidden_weights)


        output_weights = np.random.rand(self.hidden.hidden_layers_size, self.output.output_size)
        print("Output weights: ", output_weights.shape)
        weights.append(output_weights)

        return weights

    def forwardPass(self, data):
        self.initialize_input_data(data)
        self.input.output_size = self.hidden.hidden_layers_size

        # INITIALIZE THE WEIGHTS FOR ALL LAYERS HIDDEN LAYERS + 1 WEIGHTS
        # This implies that there are weight matrices for:
        # The step between input and hidden (1)
        # A weight matrix for all the hidden layers
        # A weight matrix for the output layer

        # Set output of input to be the same as hidden layer
        #current_output = self.input.output
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

# 3 Hidden layers (3 Columns)
# Each layer has 50 neurons (Rows)
# Activation function is a given activation function such as Relu
hidden_layers = Layer(3, 50, Relu)



# 10 Classifications (0-9) Output size is 10
# Activation function is a given activation function such as Relu
output = Layer(10, Relu)

nn = NeuralNetwork(input,hidden_layers,output)

nn.train(flattenedData)

#print(nn.input.initialized_input)
print(len(nn.weights_array))
print("Activation function for hidden layers: ", nn.hidden.activation_function)
