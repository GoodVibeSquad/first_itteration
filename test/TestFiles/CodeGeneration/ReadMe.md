# Code Generation Tests Readme
## 1. Empty Program
```
{}
```
Expected: Fails due to an empty block having no valid statements. Should throw a RuntimeException.
## 2. Print None
```
{
    print();
}
```
Expected: Fails because print is missing an expression. Should throw a RuntimeException.
## 3. For Loop with Zero Iterations
```
{
    for (int i = 0; i < 0; i++;) {
        print(i);
    }
}
```
Expected Output: (empty) — loop body should never execute.

## 4. Infinite While Loop (with manual break)
```
{
    int i = 0;
    while (true) {
        print(i);
        break;
    }
}
```
Expected Output:
```
0
```
## 5. Invalid Constructor Arguments
```
{
    NeuralNetwork nn = new NeuralNetwork(7);
}
```
Expected: Type checker fails due to argument mismatch for constructor. Should throw a RuntimeException.

## 6. Variable Shadowing
```
{
    int x = 5;
    {
        int x = 10;  // Illegal shadowing
    }
}
```
Expected: Type checker fails due to argument mismatch for constructor. Should throw a RuntimeException.

## 7. Empty If-Else Block
```
{
    if (true) {
    } else {
    }
}
```
Expected: Fails because both branches are empty. Should throw a RuntimeException.

## 8. Ternary with Mixed Types
```
{
    bool condition = true;
    int x = condition ? 7 : "hello";
}
```
Expected: Fails due to mixed result types. Should throw a RuntimeException.

## 9. Unsupported Type Conversion
```
{
    bool b = true;
    int x = (int)b;  // Not allowed
}
```
Expected: Fails due to unsupported conversion. Should throw a RuntimeException.

## 10. Incomplete Assignment
```
{
    int x = ;
}
```
Expected: Fails due to syntax error. Should throw a RuntimeException.


## 11. Valid basic print
```
{
    print(5);
}

```

## 12. Valid constructur use
```
{
    Layer l1 = new Layer(10);
    Layer l2 = new Layer(10, 3, "Relu");
    Layer l3 = new Layer(10, "Softmax");
    NeuralNetwork nn = new NeuralNetwork(l1, l2, l3);
    print("constructed");
}


```
## 13. Valid ternary use
```
{
    bool cond = true;
    int x = cond ? 1 : 2;
    print(x);
}

```

## 14. Loop iteration
```
{
    double piVal = PI;
    print(piVal);
}

```

## 15. Math library use
```
{
    double piVal = PI;
    print(piVal);
}

```