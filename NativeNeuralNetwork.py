# REQUIREMENT: Install Numpy

import numpy as np
from PIL import Image
from collections import defaultdict
import os
import pickle



# ACTIVATION FUNCTION INFO

class activationFunction:
    def run(x):
        return x
    def derivative(x):
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
            self.activation_function = globals()[args[1]]

        # For multiple hidden layers
        # Amount is how many hidden layers there are
        # Size is neuron amount
        elif len(args) == 3:
            self.amount = args[0]
            self.size = args[1]
            self.activation_function = globals()[args[2]]

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
    def __init__(self, *args):


        if(len(args) == 3):
            self.input = args[0]
            self.hidden_layers = args[1]
            self.output = args[2]
            self.weights_array = self.init_weights()
            self.activation_functions = self.init_activation_functions()
            self.bias = self.init_bias()
            self.classification = []
        elif(len(args) == 1):
            self.load_model(args[0])




    def load_model(self, filepath):
        with open(filepath, 'rb') as file:
                model_data = pickle.load(file)
        self.weights_array = model_data['weights_array']
        self.bias = model_data['bias']
        self.classification = model_data['classification']
        self.input = model_data['input']
        self.hidden_layers = model_data['hidden_layers']
        self.output = model_data['output']
        self.activation_functions = self.init_activation_functions()

    def save(self, filepath):
        model_data = {
            'weights_array': self.weights_array,
            'bias': self.bias,
            'classification': self.classification,
            'input': self.input,
            'hidden_layers': self.hidden_layers,
            'output': self.output
        }
        with open(filepath, 'wb') as file:
            pickle.dump(model_data, file)



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
        output_weights = np.random.rand(self.hidden_layers.size, self.output.output_size) * np.sqrt(2. / self.hidden_layers.size)
        weights.append(output_weights)

        return weights



    def forwardPass(self, data, subfolder, subfolder_index_image):

        activations = []

        # Initialize the current input to be the initialized data
        current_input = (data[subfolder])[subfolder_index_image]

        # Calculates weighted sum for everything except output
        for i in range(self.hidden_layers.amount + 1):
            # Add positive bias (Number between 0 and 1) after the weighted sum
            # BEFORE THE ACTIVATION

            # Calculates weighted sum and adds it to weighted sum array
            # print("Weight ", i, ": ", self.weights_array[i])
            current_weighted_sum = np.dot(current_input, self.weights_array[i]) + self.bias[i]

            # Runs the activation function for Hidden layers (Found at 0th index)
            current_activation = self.activation_functions[0].run(current_weighted_sum)
            activations.append(current_activation)

            # Updates the current input and moves forward in neural network
            current_input = current_activation
        return activations

#        print("Output activation: ", output_activation)

    def init_data(self, path, datatype):
        match datatype:
            case ".png" | ".jpg" | ".jpeg" :
                return self.load_image_data(path, datatype)
            case _:
                raise ValueError(f"File must be a {datatype} image.")

    def load_image_data(self, path,datatype):
        subfolders = [ f.path for f in os.scandir(path) if f.is_dir() ]

        #for i in range(len(subfolders)):
            #print("Subfolder:", subfolders[i])
        images_array = []

        # Insert flattened images based on all subfolders
        for i in range(len(subfolders)):
            numbered_image_array = []
            self.classification.append(os.path.basename(subfolders[i]))
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
                    if flattenedData.shape[1] != self.input.input_size:
                        raise ValueError(f"Input size mismatch: expected flattened size {self.input.input_size}, got {flattened_data.shape[1]}, from {image}")
                    numbered_image_array.append(flattenedData)

            images_array.append(numbered_image_array)

        return images_array

    def backPropagate(self, activations, correct_label, learningRate, image):
        correct_answer = np.zeros((1, self.output.output_size))
        correct_answer[0, correct_label] = 1

        delta = []

        # Output layer gradient (Softmax + Cross-Entropy)
        error_output =  correct_answer - activations[-1]
        delta.append(error_output)

        # Hidden layers: from last hidden to first hidden
        for i in reversed(range(len(self.weights_array) - 1)):
            d_activation = self.activation_functions[0].derivative(activations[i])
            d = np.dot(delta[-1], self.weights_array[i + 1].T) * d_activation
            delta.append(d)

        delta.reverse()

        # Weight and bias updates
        for i in range(len(self.weights_array)):
            if i == 0:
                input_to_layer = image
            else:
                input_to_layer = activations[i - 1]
            self.weights_array[i] += learningRate * np.dot(input_to_layer.T, delta[i])
            self.bias[i] += learningRate * np.sum(delta[i], axis=0, keepdims=True)


    def printPredictions(self, validationSet,images_array):
        avrage = []
        grouped_data = defaultdict(list)
        failed = defaultdict(list)
        for i in range(len(validationSet)):
            current_picture = (images_array[validationSet[i][0]])[validationSet[i][1]]
            activations = []

            for j in range(self.hidden_layers.amount + 1):
                # Add positive bias (Number between 0 and 1) after the weighted sum
                # BEFORE THE ACTIVATION

                # Calculates weighted sum and adds it to weighted sum array
                # print("Weight ", i, ": ", self.weights_array[i])
                current_weighted_sum = np.dot(current_picture, self.weights_array[j]) + self.bias[j]

                # Runs the activation function for Hidden layers (Found at 0th index)
                current_activation = self.activation_functions[0].run(current_weighted_sum)
                activations.append(current_activation)

                # Updates the current input and moves forward in neural network
                current_picture = current_activation

            # Applies output activation function after weighted sum is finished (1st index)
            number = validationSet[i][0]
            output_activation = self.activation_functions[1].run(current_picture)
            predicted_index = np.argmax(output_activation)
            correct_answer = np.zeros(10)
            correct_answer[number] = 1
            dif =  correct_answer - output_activation
            procent = (1 - dif[0,number])*100
            avrage.append(procent)
            grouped_data[number].append(procent)
            if not (predicted_index == number):
                failed[number].append(procent)
        
        print("Acuracy: ", (len(avrage) - sum(len(v) for v in failed.values()))/len(avrage) * 100)
        print("\nSuccesses: ",len(avrage) - (sum(len(v) for v in failed.values())), " out of", len(avrage))
        for number in failed:
            print("classification: ", self.classification[number], "\n \t",len(grouped_data[number]) - len(failed[number]), " out of ", len(grouped_data[number]))




    def init_one_data(self, path, datatype):
                match datatype:
                    case ".png" | ".jpg" | ".jpeg" :
                        return self.load_one_image_data(path)
                    case _:
                        raise ValueError(f"File must be a {datatype} image.")


    def load_one_image_data(self, path):
        # Open image, convert to grayscale
        img = Image.open(path).convert('L')
        numpy_data = np.array(img)
        normalized_data = numpy_data / 255.0
        flattened_data = normalized_data.flatten(order='C').reshape(1, -1)

        if flattened_data.shape[1] != self.input.input_size:
            raise ValueError(f"Input size mismatch: expected flattened size {self.input.input_size}, got {flattened_data.shape[1]}")

        return flattened_data

    def predict(self, path, datatype):
        data = self.init_one_data(path,datatype)
        activations = []

        current_input = data
        for j in range(self.hidden_layers.amount + 1):
            current_weighted_sum = np.dot(current_input, self.weights_array[j]) + self.bias[j]
            current_activation = self.activation_functions[0].run(current_weighted_sum)
            activations.append(current_activation)
            current_input = current_activation

        output_activation = self.activation_functions[1].run(current_input)
        predicted_index = np.argmax(output_activation)
        predicted_label = self.classification[predicted_index]

        print("This ", datatype, " is classified as: ", predicted_label)
        print("The likelihood for each classification")
        for label, activation in zip(self.classification, output_activation.flatten()):
            print(f"\t {label} is {activation*100} %")


        return predicted_label



    def train(self, path, datatype, epochs, test_percentage, learningRate):
        # Call forward pass n times for neural network
        if not os.path.exists(path):
            if path == "mnist_example":
                dirname = os.path.dirname(__file__)
                mnist_example = os.path.join(dirname, 'Mnist')
                images_array = self.init_data(mnist_example, datatype)
            else:
                raise ValueError(f"Path {path} does not exist.")
        else:
            images_array = self.init_data(path,datatype)
        training_set = []
        validation_set = []
        # random selction of images
        for i in range(len(images_array)):
            # Takes 70 percent of images (Rest will be used for validation)
            training_amount = int(len(images_array[i])/100 * test_percentage)
            #print("the ", i, "Training set amount", training_amount)

            validation_amount = len(images_array[i]) - training_amount
            #print("the ", i, "Validation set amount", validation_amount)

            for x in range(training_amount):
                training_set.append([i,x])
            for x in range(validation_amount):
                validation_set.append([i,x+training_amount])

        np.random.shuffle(training_set)

        #print("Training set length: ", len(training_set))
        #print("validation set length: ", len(validation_set))
        # Second parameter is the subfolders in this example (0th subfolder)
        # Third parameter is the index of a given image in the subfolder

        for _ in range(epochs):
            for x in range(len(training_set)):
                activations = self.forwardPass(images_array,training_set[x][0], training_set[x][1])
                self.backPropagate(activations, training_set[x][0],learningRate, images_array[training_set[x][0]][training_set[x][1]])

        self.printPredictions(validation_set,images_array)


##### NEURAL NETWORK STUFF
# Layer needs to take the width- of a matrix and the height of a matrix
# We imagine the images to be 28px * 28px images such as Mnist dataset
# This creates a vector with 784 columns and 1 row

# The input layer contains the data
# The output is automatically matched with the neuron size of the hidden layers
#input = Layer(28*28)

# 5 Hidden layers (5 Columns)
# Each layer has 130 neurons (Rows)
# Activation function is a given activation function such as Relu
#hidden_layers = Layer(5, 130, "Relu")



# 10 Classifications (0-9) Output size is 10
# Activation function is a given activation function such as Relu
#output = Layer(10, "Softmax")

#nn = NeuralNetwork(input,hidden_layers,output)

# Source directory

#### TODO:
# When we get further make it so users can manually insert filepath as a string
# In the native code

# Get path for a given image in root



#nn.train("mnist_example", ".png", 20, 70, 0.001)

#nn.save("saved_model.pkl")
