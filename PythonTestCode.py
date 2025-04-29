import math
# WE ASSUME THE PARSER CHECKS THE DATATYPE THEREFORE WE DO NOT PERFORM
# EXPLICIT TYPE CONVERSION IN PYTHON. UNLESS DONE SO IN SOURCE CODE.


# Hashtag in python is equivelant to // in our language
# // THIS IS A COMMENT IN OUR LANGUAGE
# /* THIS IS A
# MULTI LINE COMMENT IN OUR LANGUAGE
# */


# VARIABLE DEFINITION BELOW:
# x = 5 in python is equivelant to int x = 5 in our language.
# x = "Hello" in python is equivelant to String x = "Hello" in our language.
# x = True in python is equivelant to Bool x = true in our language.
# x = 5.5 in python is equivelant to Double x = 5.5 in our language.
# x = variableName in python is equivelant to Datatype x = variablename

# EXAMPLES FOR VARIABLE DEFINITION BELOW:
x = 5
x = "Hello"
x = True
x = 5.5


# LOOP SECTION BELOW
# NOTE: PYTHON HAS NO CLEANUP OF VARIABLES. THEY ARE SAVED INDEFINITELY
# FOR THE AFOREMENTIONED REASON WE MUST REMOVE VARIABLES MANUALLY WITH [del variable]


# For each loop in our code(WE ASSUME A LIST WITH INTEGERS INTLIST EXISTS)
#   for (int i : intList) {
#    print(i)
#   }

## For each loop in Python (Means for EACH val IN z):
z = [1, 2, 3, 4]
for exampleValue in z:
   print(exampleValue)
del exampleValue


# NOTE: We can't use normal for loops in python, so we have to do it in a different way
#   for (int i = 0; i < 10; i++) {
#    print(i)
#   }

# For loop in python (Based on our interpretation)
i = 0
while i < 10:
    i += 1
del i


# While loops in our code (Here you can assume expression might be an equality x > 0)
# while(Expression){
#   Statement
# }


# While loops in python
i = 0
while i < 10:
#    print(i)
    i += 1
# REMINDER: DELETE ALL NEW VARIABLES CREATED IN WHILE LOOP UPON COMPLETION


# ARRAYS IN OUR LANGUAGE
# int[] arr = [10, 20, 30]

# Arrays in python
x = [10, 20, 30]


# OPERATORS BELOW:

# OUR CODE VERSION (Addition)
# int x = 5 + 3;

# PYTHON VERSION (Addition)
x = 5 + 3


# OUR CODE VERSION (Subtraction)
# int x = 5 - 3;

# PYTHON VERSION (Subtraction)
x = 5 - 3


# OUR CODE VERSION (Multiplication)
# int x = 5 * 3;

# PYTHON VERSION (Multiplication)
x = 5 * 3


# OUR CODE VERSION (Division)
# int x = 5 / 3;

# PYTHON VERSION (Division)
x = 5 / 3


# OUR CODE VERSION (Modulus)
# int x = 5 % 3;

# PYTHON VERSION (Modulus)
x = 5 % 3


# OUR CODE VERSION (Exponentiation)
# int x = 5 ^ 3;

# PYTHON VERSION (Exponentiation)
x = 5 ** 3


# COMPARISON OPERATORS:

# OUR CODE VERSION (Less than)
# bool result = 5 < 3;

# PYTHON VERSION (Less than)
result = 5 < 3


# OUR CODE VERSION (Greater than)
# bool result = 5 > 3;

# PYTHON VERSION (Greater than)
result = 5 > 3


# OUR CODE VERSION (Equal to)
# bool result = 5 == 3;

# PYTHON VERSION (Equal to)
result = 5 == 3


# OUR CODE VERSION (Not equal to)
# bool result = 5 != 3;

# PYTHON VERSION (Not equal to)
result = 5 != 3


# OUR CODE VERSION (TERNARY OPERATOR)
# int x = (a > b) ? a : b;

# PYTHON VERSION
a = 0
b = 5
x = a if a > b else b
# This would produce b, because a is not greater than b

# LOGICAL OPERATORS

# OUR CODE VERSION (Logical OR)
# bool result = (true | false);

# PYTHON VERSION (Logical OR)
result = (True or False)
# Result = True in this case


# OUR CODE VERSION (Logical AND)
# bool result = (true & false);

# PYTHON VERSION (Logical AND)
result = (True and False)
# Result = False in this case


# INCREMENT AND DECREMENT
# NOTE: WE SHOULD CONSIDER IMPLICATIONS IN TERMS OF FOR LOOPS WITH POST AND PREFIX INCREMENTS/DEC

# OUR CODE VERSION (Post-increment)
# int x = 5;
# x++;

# PYTHON VERSION (Increment)
x = 5
x += 1


# OUR CODE VERSION (Post-decrement)
# int x = 5;
# x--;

# PYTHON VERSION (Decrement)
x = 5
x -= 1


# ASSIGNMENT OPERATORS

# OUR CODE VERSION (Addition Assignment)
# int x = 5;
# x += 3;

# PYTHON VERSION (Addition Assignment)
x = 5
x += 3


# SYMBOLS

# OUR CODE VERSION (If Statement)
# if (x == 5) {
#     print("Hello");
# }

# PYTHON VERSION (If Statement)
if x == 5:
    print("Hello")



# OUR CODE VERSION (ELSE / ELSE IF Statements)
# if (x == 5) {
#     print("Yes");
# } else if (x == 6) {
#     print("Maybe");
# } else {
#     print("No");
# }

# PYTHON VERSION
if x == 5:
    print("Yes")
elif x == 6:
    print("Maybe")
else:
    print("No")

# OUR CODE VERSION (MAXIMUM VALUE)
# int maxVal = Max(0,1);

# PYTHON VERSION
maxVal = max(0, 1)



# OUR CODE VERSION (SUM)
# int sum = Sum(i,n,id,Expression);

# RUNS n TIMES FROM i(including i) expression is interchangeable for value k
# FIRST K IS WHERE YOU INSERT THE EXPRESSION IN THE SUM VALUE (THe function for the sum)
# PYTHON VERSION
i = 1
n = 2  # Adjust n for a proper range
sum_value = sum(k**2 for k in range(i, n+1))  # Summing squares of numbers from i to n-1
print("SUM")
print(sum_value)



# OUR CODE VERSION (SQUARE ROOT)
# double sqrt = Sqrt(16);

# PYTHON VERSION
sqrt_value = math.sqrt(16)


# CONSTANTS

# OUR CODE VERSION
# double euler = E;

# PYTHON VERSION
euler = math.e


# OUR CODE VERSION
# double pi = Pi;

# PYTHON VERSION
pi = math.pi


# OUR CODE VERSION (Activation Functions)
# activationFunction Relu = new activationFunction.Relu;
# activationFunction SigmaFunc = new activationFunction("other activationFunction");

# PYTHON VERSION (Creating RELU activationFunc example)
class activationFunction:
    def run(x):
        return x

    def amazingMethod(x):
        return x

# Relu can use the amazingMethod!
class Relu(activationFunction):
    def run(x):
        return max(0,x)


# REMEMBER TO MAKE PARENTHESES AND TUBORG KLAMMER (TABULATION IN PYTHON)