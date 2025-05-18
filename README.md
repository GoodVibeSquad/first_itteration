# first_itteration


REQUIREMENTS:

Install Python 

## Requiered installations
- pip install numpy
- pip install pillow
- pip install pickle


## Using the Language
This example will walkthrough the process of using the language to create a simple program that can train a neuralnetwork and predict an image classification.

## Example
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
NeuralNetwork nn2 = new NeuralNetwork("mnist_example.pkl");

//it is required to use the absoulte path of the image
nn2.predict("C:/Users/phili/Documents/GitHub/first_itteration/Mnist/3/32.png", ".png");

```


