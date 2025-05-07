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
        return np.maximum(0,x)

class Softmax(activationFunction):
    def run(x):
        e_x = np.exp(x - np.max(x))  # for numerical stability
        return e_x / e_x.sum(axis=1, keepdims=True)

class Layer:
    def __init__(self, *args):
        # For input layer
        # Layer(Input data size)
        if len(args) == 1:
            self.input_size = args[0]

            # Determined later when making hidden layers
            self.output_size = 0

            self.initialized_input = 0

        # For output layers
        # Loss function is found on the neural network itself
        # For output layer
        elif len(args) == 2:
            self.output_size = args[0]
            self.activation_function = args[1]

        # For multiple hidden layers
        elif len(args) == 3:
            self.amount = args[0]
            self.size = args[1]
            self.activation_function = args[2]

        else:
            raise ValueError(
                f"Layer() takes 1, 2, or 3 arguments, but {len(args)} were given: {args}. "
                "Expected usage:\n"
                " - Layer(input_size)\n"
                " - Layer(output_size, activation_function)\n"
                " - Layer(num_hidden_layers, layer_size, activation_function)"
            )


# ADDING LOSS FUNCTION TO NEURAL NETWORK



class NeuralNetwork:
    def __init__(self, input, hidden_layers, output):
        self.input = input
        self.hidden_layers = hidden_layers
        self.output = output
        self.weights_array = self.init_weights()
        self.activation_functions = self.init_activation_functions()

    def initialize_input_data(self, data):
        self.input.initialized_input = data

    # Get activations for every layer except for input
    def init_activation_functions(self):
        activation_functions = [
            self.hidden_layers.activation_function,
            self.output.activation_function
        ]
        return activation_functions


    def init_weights(self):
        weights = []

        # Generating weights between first layer(input) and first hidden layer
        input_weights = np.random.rand(self.input.input_size, self.hidden_layers.size)
        print("Input weights: ", input_weights.shape)
        weights.append(input_weights)

        # LOOP FOR GENERATING WEIGHTS FOR HIDDEN LAYERS
        # Hidden layer to hidden layer connections
        for i in range(self.hidden_layers.amount - 1):
            hidden_weights = np.random.rand(self.hidden_layers.size, self.hidden_layers.size)
            print("Hidden weight: ", hidden_weights.shape)
            weights.append(hidden_weights)

        # Generating weights between last layer(output) and last hidden layer
        output_weights = np.random.rand(self.hidden_layers.size, self.output.output_size)
        print("Output weights: ", output_weights.shape)
        weights.append(output_weights)

        return weights


    def weighted_sum(self, data, weight_matrix):
        return np.dot(data, weight_matrix)



    def forwardPass(self, data):
        weighted_sums = []
        activations = []

        # Initialize the current input to be the initialized data
        current_input = data

        # Calculates weighted sum for everything except output
        for i in range(self.hidden_layers.amount):
            # Calculates weighted sum and adds it to weighted sum array
            current_weighted_sum = np.dot(current_input, self.weights_array[i])
            weighted_sums.append(current_weighted_sum)

            # Runs the activation function for Hidden layers on the 0th index
            # of the activation functions array
            current_activation = self.activation_functions[0].run(current_weighted_sum)
            activations.append(current_activation)

            # Updates the current input and moves forward in neural network
            current_input = current_activation

        # Calculates final weighted sum from last hidden layer to output
        # Applies output activation (softmax) and finishes forward pass
        final_weighted_sum = np.dot(current_input, self.weights_array[-1])
        weighted_sums.append(final_weighted_sum)
        output_activation = self.activation_functions[1].run(final_weighted_sum)
        activations.append(output_activation)
        print("Output activation: ", output_activation)


    def train(self, data):
        # Call forward pass n times for neural network
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
# Layer needs to take the width- of a matrix and the height of a matrix
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
output = Layer(10, Softmax)

nn = NeuralNetwork(input,hidden_layers,output)

nn.train(flattenedData)

#print(nn.input.initialized_input)
print("Length of weights array: ",len(nn.weights_array))
print("Activation function for hidden layers: ", nn.hidden_layers.activation_function)
