## 1. Test Shift & Reduce From TableGenerator
```
//input
7;         

//7 should reduce to Slist[SExpression(Econstant(CInt(7)))]
//if it does we can assume that it shifts and reduces

```
## 2. Testing Constants
```
7;
3.14;
true;
"test"
Pi;
E;      
```

## 3. Testing Valid Expressions
```
(3);
7 + 7;
7 - 7;
7 * 7;
7 / 7;
7 % 7; 
-10;
x = true;
!x;
Print(x);
Sum(6, 1 , i, i+1);
Sqrt(7+7);
Max(1,7,6,7,4,4,4,23,5,64,6,6);
(double) 7;
x ? "hej": "nej";
new NeuralNetwork(7,8,9);
------------------------------
Skal diskuteres
NeuralNetwork.load(123123);
------------------------------
nn.myFunction(123123);


```

## 4. Testing Syntax Errors

```
(3;                  // Missing parentheses
7 +;                 // Incomplete binary operations
7 + 7                // Missing semicolon
7 ++ 7;              // Double operators
```

## 5. Testing Function Call Errors
```		
print();             // Missing arguments (giver dette fejl i vores kode?)
Sum(6,,1);           // Extra comma
print(x;             // Missing closing parenthesis
Max(,1,2);           // Invalid argument lists
```

## 6. Testing Type Cast Errors
```
() 7;                // Missing target type
(double 7);          // Invalid cast syntax
(double);            // Missing expression
```
## 7. Testing Ternary Operator Errors
```
x ? "yes" "no";      // Missing colon
? "yes" : "no";      // Missing condition
x ? : "no";          // Missing then branch

```

## 8. Testing Object Construction/Method Calls
```

new (7,8,9);         // Invalid new syntax
new NeuralNetwork(); // Missing constructor arguments:
nn.();               // Invalid method call syntax
nn.(123);            // Missing method name
```

## 9. Simple Statements
```
x = 5;                          // Assignment
int y = 10;                     // Declaration with initialization
int z;                         // Declaration only
x++;                          // Increment
y--;                          // Decrement
Print("Hello");               // Function call as statement
```

## 10. If Statements
```
if (x > 0) {                 
    y = 2;
}

if (x == 0) {
    y = 5;
} else {
    z = 6;
}
```

## 11. Nested If Statements
```
if (x > 0) {
    if (y > 0) {
        z = 1;
    } else {
        z = 2;
    }
}
```

## 12. For Loops
```
for (int i = 0; i < 10; i++) {
    x = i;
}
for (i = 0; i < 3; i++) {             // Using existing variable
    Print(i);
}
```

## 13. While Loops
```

while (x > 0) {
    x--;
}
while (true) { break };              // With break
```

## 14. Break/Continue
```
for (int i = 0; i < 10; i++) {
    if (i == 5) { break; }
    if (i == 2) { continue; }
    x = i;
}
```

## 15. Nested Loops
```
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        x = i + j;
    }
}
```

## 16. Block Statements
```

{
    int a = 1;
    int b = 2;
    a = a + b;
}
```

## 17. Mixed Complex Statements
```
int count = 0;
while (count < 3) {
    if (count == 1) {
        for (int i = 0; i < 2; i++) {
            Print(i);
        }
    } else {
        count++;
    }
}
```

## Testing Statements
```
int x = 0;
if(true) {
x = 5;          //also makes an sblock
} else{
x=8;
}

for (int x = 0; x < 10; i++;) {
    x++;
    break;
    
}

for (int x = 0; x < 10; i++;) {
    x++;
    continue; 
    
}

------------------------------
Skal diskuteres
while x < 10 x++;
------------------------------



for (int x = 0; x < 10; i++;) {
//should produce an error 
}

```





