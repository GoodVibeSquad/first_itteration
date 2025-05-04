class Layer:
    def __init__(self, *args):
        if len(args) == 1:
            self.rowSize = args[0]
            self.columnSize = 1
        if len(args) == 2:
            self.rowSize = args[0]
            self.columnSize = args[1]
    def __str__(self):
        return f"Row size: {self.rowSize}\nColumn size: {self.columnSize} "

# Layer needs to take the width of a matrix and the height of a matrix
input = Layer(6, 6)

# If a layer only has one argument. Automatically set column size to 1.
inputOneArg = Layer(6)

print(str(input) + "\n")
print(inputOneArg)
