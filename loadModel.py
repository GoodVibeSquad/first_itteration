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
        output_weights = np.random.rand(self.hidden_layers.size, self.output.output_size)
        weights.append(output_weights)

        return weights


    def forwardPass(self, data, i, x):
        weighted_sums = []
        activations = []

        # Initialize the current input to be the initialized data
        current_input = (data[i])[x]

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
                    numbered_image_array.append(flattenedData)

            images_array.append(numbered_image_array)

        return images_array

    def backPropagate(self, activations,correct_label, learningRate, image):
        correct_answer = np.zeros(10)
        correct_answer[correct_label] = 1
        error = []
        delta = []

        #softmax stuff
        error_output = correct_answer - activations[-1]
        error.append(error_output)
        delta.append(error_output)

        for i in reversed(range(len(activations)-2)):
            error.append(np.dot(delta[-1],self.weights_array[i+1].T))
            delta.append(error[-1] * Relu.derivative(activations[i]))
        #delta is created from end to start
        delta.reverse()
        for i in range(len(self.weights_array)):
            if(i<=0):
                tempVar = np.dot(image.T, delta[i])
                self.weights_array[i] += learningRate * tempVar
                self.bias[i] += learningRate * delta[i]
                break
            tempVar = np.dot(activations[i].T, delta[i])
            self.weights_array[i] += learningRate * tempVar
            self.bias[i] += learningRate * delta[i]


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
            if (predicted_index == number):
                failed[number].append(procent)
            #print(procent)
        print("average %: ", np.sum(avrage)/len(avrage))
        avrageprocent = {number: sum(percentages) / len(percentages) for number, percentages in grouped_data.items()}
        for number in avrageprocent:
            print("classification: ", self.classification[number], "percentage: ", avrageprocent[number])
        print("\nfailed: ", sum(len(v) for v in failed.values()), " out of", len(avrage))
        for number in failed:
            print("classification: ", self.classification[number], "\n \t", len(failed[number]), " out of ", len(grouped_data[number]))
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

        return predicted_label



    def train(self, path, datatype, epochs, test_percentage, learningRate):
        # Call forward pass n times for neural network
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

nn = NeuralNetwork("saved_model.pkl")
prediction = nn.predict(r"D:\SW4\P4\first_itteration\Mnist\5\23.png",".png")
