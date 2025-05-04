
# REQUIREMENT: Install Numpy

import numpy as np

class Layer:
    def __init__(self, *args):
        if len(args) == 1:
            self.columns = args[0]
            self.rows = 1
        if len(args) == 2:
            self.columns = args[0]
            self.rows = args[1]
        self.weights = self.init_weights();
    def __str__(self):
        return (f"Columns: {self.columns}\nRows: {self.rows}\n"
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


# Layer needs to take the width of a matrix and the height of a matrix
# Imaginary situation with 28 * 28 images

input = Layer(28*28, 6)

# Hidden layer is imagined with 50 neurons
hidden = Layer(50,6)
output = Layer(10, 6)

nn = NeuralNetwork(input,hidden,output)

# If a layer only has one argument. Automatically set column size to 1.
# inputOneArg = Layer(6)
# print(str(input) + "\n")

print(nn)
