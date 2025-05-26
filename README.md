# first_itteration


REQUIREMENTS:

Install Python 

## Requiered installations
- pip install numpy
- pip install pillow
- pip install pickle
- pip install matplotlib

## Using the Language
This example will walkthrough the process of using the language to create a simple program that can train a neuralnetwork and predict an image classification.

## Example
First create a file for creating your Neura code. This should be a simple txt file.

Next set up a neural network as shown below:
```
//Image size is 28x28
Layer input = new Layer(28*28);

//hidden layer with 5 hidden layers and 130 neurons, and Relu activation function
Layer hidden = new Layer(5, 130,"Relu");

//output layer with 10 classifications and Softmax activation function
//the activation function must be Softmax for the output layer
Layer output = new Layer(10,"Softmax");

NeuralNetwork nn = new NeuralNetwork(input, hidden, output);

//mnist_example is a predefined folder that contains the mnist dataset
//if you want to use your own dataset, you can create a folder with the same structure as mnist_example
//you will then need to use the absolute path of the folder
nn.train("mnist_example", ".png", 20, 70, 0.01);

//to save the model, you need to use the absolute path of the model
// if you don't use absolute path, it will save the model in the current directory
nn.save("mnist_example.pkl");


//to load the model, you need to use the absolute path of the model
// if you don't use absolute path, it will look for the model in the current directory
// if you are using windows, you must replace all backslashes(\) with either two backslashes(\\) or single forward slashes (/)
NeuralNetwork nn2 = new NeuralNetwork("mnist_example.pkl");

//it is required to use the absoulte path of the image
nn2.predict("YOUR_FILEPAH", ".FILETYPE");

```
With the neural network file created. Run the compiler and insert the absolute path for the code file.

A python file should now be in the same directory as the original .txt file, which can now be run in the terminal.

# Documentation
Neura is a high-level programming language designed for building, training, and deploying neural networks in a concise and readable syntax. It provides native types for neural network constructs and compiles to Python for execution.

## Types
### Primitive types
- int: integers
- double: Floating-point number
- string: Unicode String
- bool: Boolean (true / false)
### Special types
- Layer: Represents a layer in a neural network. Constructed with image size and activation
- NeuralNetwork: Represents a complete neural network model
- ActivationFunction: Represents a function such as "Relu" or "Softmax" 
## Variables
- ``int x = 7;``
- ``string y = "test";``
- ``double z = 7.5;``
- ``bool n = true; ``
- ``Layer input = new Layer(28x28)``
- ``Layer hidden = new Layer(5, 130,"Relu");``
- ``Layer output = new Layer(10,"Softmax");``
- ``NeuralNetwork nn = new NeuralNetwork(input, hidden, output);``
- ``NeuralNetwork nn2 = new NeuralNetwork("mnist_example.pkl");``
## Expressions
Supports arithmetic, logical, and comparison expressions
```
int sum = 3 + 4 * 5;
bool isValid = (x > 0) && (y < 100);
```
## Statements
### Initialization
``int x = 10:``
### Assignment
``x = x + 1;``
### If / Else
```
if (isTraining) {
code
} else {
code
}
```
### While
```
while (epoch < 10) {
    code block
}
```
## Functions
User-defined functions
```
int add(int a, int b) {
    code 
}
```
## Methods
```
nn.train(data, labels);
nn.save("model.nn");
nn.predict("imagePath", "fileType");
```
## Comments
```
// this is a single line comment

/* 
 This is a multi line comment
*/
```




















