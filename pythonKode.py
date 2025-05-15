import math
input = Layer(784)
hidden = Layer(5, 130)
output = Layer(10)
nn = NeuralNetwork(input, hidden, output)
path = "hej"
nn.save(path)