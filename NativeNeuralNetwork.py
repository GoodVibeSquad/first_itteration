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

    def derivative(x):
        return (x > 0) * 1

class Softmax(activationFunction):
    def run(x):
        e_x = np.exp(x)
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
        # Amount is how many hidden layers there are
        # Size is neuron amount
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
        self.bias = self.init_bias()


    def init_bias(self):
        bias = []


        for i in range(self.hidden_layers.amount):
            bias.append(np.random.rand(1, self.hidden_layers.size))

        output_bias = np.random.rand(1, self.output.output_size)
        bias.append(output_bias)

        return bias

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


        # HE initilization used because of RELU and exploding rand weight values

        # Calculate first weights between input and hidden
        input_weights = np.random.randn(self.input.input_size, self.hidden_layers.size) * np.sqrt(2. / self.input.input_size)
        # Rounding to improve readability
        input_weights = np.round(input_weights, decimals=10)
        weights.append(input_weights)

        # Generate weights between hidden layers
        for i in range(self.hidden_layers.amount - 1):
            hidden_weights = np.random.randn(self.hidden_layers.size, self.hidden_layers.size) * np.sqrt(2. / self.hidden_layers.size)
            weights.append(hidden_weights)

        # Generating weights between last layer(output) and
        # the second to last layer, which is a hidden layer.
        output_weights = np.random.rand(self.hidden_layers.size, self.output.output_size)
        weights.append(output_weights)

        return weights


    def forwardPass(self, data):
        weighted_sums = []
        activations = []

        # Initialize the current input to be the initialized data
        current_input = data

        # Calculates weighted sum for everything except output
        for i in range(self.hidden_layers.amount + 1):
            # Add positive bias (Number between 0 and 1) after the weighted sum
            # BEFORE THE ACTIVATION

            # Calculates weighted sum and adds it to weighted sum array
            # print("Weight ", i, ": ", self.weights_array[i])
            current_weighted_sum = np.dot(current_input, self.weights_array[i]) + self.bias[i]
            weighted_sums.append(current_weighted_sum)

            # Runs the activation function for Hidden layers (Found at 0th index)
            current_activation = self.activation_functions[0].run(current_weighted_sum)
            activations.append(current_activation)

            # Updates the current input and moves forward in neural network
            current_input = current_activation

        # Applies output activation function after weighted sum is finished (1st index)
        output_activation = self.activation_functions[1].run(current_input)
        activations.append(output_activation)

        print("Output activation: ", output_activation)

    def init_data(self, path, datatype):
        match datatype:
            case ".png" | ".jpg" | ".jpeg" :
                return self.load_image_data(path, datatype)
            case _:
                pass

    def load_image_data(self, path,datatype):
        subfolders = [ f.path for f in os.scandir(path) if f.is_dir() ]

        #for i in range(len(subfolders)):
            #print("Subfolder:", subfolders[i])
        images_array = []

        # Insert flattened images based on all subfolders
        for i in range(len(subfolders)):
            numbered_image_array = []
            for images in os.listdir(subfolders[i]):
                # check if the image ends with png
                if (images.endswith(datatype)):
                    image = os.path.join(subfolders[i], images)
                    # Ensures greyscale mode
                    img = Image.open(image).convert('L')

                    # Convert to a NumPy array and flatten it
                    numpyData = np.array(img)
                    normalized_data = numpyData / 255.0
                    np.set_printoptions(threshold=np.inf)
                    flattenedData = normalized_data.flatten(order='C').reshape(1, -1)
                    numbered_image_array.append(flattenedData)

            images_array.append(numbered_image_array)

        return images_array

    def train(self, path, datatype):
        # Call forward pass n times for neural network
        images_array = self.init_data(path,datatype)

        for i in range(len(images_array)):
            print("Contents images array for ", i, ": ", len(images_array[i]))

        print("Length of images array: ", len(images_array))

        #self.forwardPass(data)


#### GETTING THE DATA

# Source directory
dirname = os.path.dirname(__file__)

# Get path for a given image in root
mnist_directory = os.path.join(dirname, 'Mnist')

zero_directory = os.path.join(mnist_directory, '0')

#subfolders = [ f.path for f in os.scandir(mnist_directory) if f.is_dir() ]

#for i in range(len(subfolders)):
#    print("Subfolder:", subfolders[i])

first_0_image = os.path.join(zero_directory, '3.png')

#print(zero_directory)

#images_array = []

# Instead of folder dir insert the directory of desired number
# Currently this is a test for the 0th subfolder (with images of 0s)
#for i in range(len(subfolders)):
#    numbered_image_array = []
#    for images in os.listdir(subfolders[i]):
#        # check if the image ends with png
#        if (images.endswith(".png")):
#            image = os.path.join(subfolders[i], images)
#            # Ensures greyscale mode
#            img = Image.open(image).convert('L')

#            # Convert to a NumPy array and flatten it
#            numpyData = np.array(img)
#            normalized_data = numpyData / 255.0
#            np.set_printoptions(threshold=np.inf)
#            flattenedData = normalized_data.flatten(order='C').reshape(1, -1)
#            numbered_image_array.append(flattenedData)

#    images_array.append(numbered_image_array)

#for i in range(len(images_array)):
#    print("Shape: ", images_array[i][0].shape, "Number class: ", images_array[i][1])

#for i in range(len(images_array)):
#    print("Contents images array for ", i, ": ", len(images_array[i]))


#print("Length of images array: ", len(images_array))

#print("Mnist dir", mnist_directory)


# For now we are using sample image
# Open the image and convert it to grayscale (L mode is greyscale mode)
img = Image.open(first_0_image).convert('L')

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

# Source directory
dirname = os.path.dirname(__file__)

# Get path for a given image in root
mnist_directory = os.path.join(dirname, 'Mnist')
nn.train(mnist_directory, ".png")

#print(nn.input.initialized_input)
print("Length of weights array: ",len(nn.weights_array))
print("Activation function for hidden layers: ", nn.hidden_layers.activation_function)
