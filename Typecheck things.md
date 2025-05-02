## 1. Basic Variable Declaration and Usage 
```
{
    int x;
    int y = 10;
    double z = 3.14;
    x = 5;
    y = x + 10;
    z = x + y;
}
```
## 2. Type Mismatch Errors
```
{
    int x = 5;
    bool b = true;
    x = b;          // Type mismatch error
    b = x;          // Type mismatch error
    double d = "hello";  // Type mismatch error
}
```
## 3. Undeclared Variable Usage
```
{
    x = 10;         // Undeclared variable error
    int y = z + 5;  // Undeclared variable error
}
```
## 4. If-Else Statements and Conditions
```
{
    int x = 10;
    bool condition = true;
    
    if(condition) {
        x = 20;
    } else {
        x = 30;
    }
    
    if(x > 25) {    // Test numeric comparison
        x = 40;
    }
    
    if(5) {         // Error: non-boolean condition
        x = 50;
    }
}
```
## 5. Loops and Break/Continue
```
{
    int i = 0;
    
    while(i < 10) {
        i = i + 1;
        if(i == 5) {
            continue;
        }
        if(i == 8) {
            break;
        }
    }
    
    break;          // Error: break outside loop
    
    for(int j = 0; j < 5; j++;) {
        int x = j * 2;
    }
}
```
## 6. Nested Scopes and Variable Shadowing
```
{
    int x = 10;
    {
        int x = 20;  // Shadows outer x
        x = 30;      // Modifies inner x
    }
    x = 40;          // Modifies outer x
    
    {
        int y = 50;
    }
    y = 60;          // Error: y not visible in this scope
}
```
## 7. Operators and Expressions
```
{
    int x = 5;
    int y = 10;
    double d = 3.14;
    bool b = true;
    
    int z = x + y * 2;
    double e = d * x;
    bool c = b && (x > y);
    bool f = !b || (x == y);
    
    int error1 = b + x;        // Error: can't add bool and int
    bool error2 = x && y;      // Error: logical op requires booleans
    bool error3 = d > "test";  // Error: can't compare different types
}
```
## 8. Complex Nested Structures
```
{
    int x = 0;
    
    if(x > 0) {
        int y = 10;
        while(y > 0) {
            y = y - 1;
            if(y == 5) {
                continue;
            }
        }
    } else {
        for(int i = 0; i < 10; i++;) {
            if(i % 2 == 0) {
                x = x + i;
            }
        }
    }
    
    while(x < 20) {
        x = x + 1;
        if(x == 15) {
            bool done = true;
            break;
        }
    }
}
```
## 9. Ternary Operator
```
{
    int x = 5;
    int y = 10;
    bool condition = true;
    
    int z = condition ? x : y;
    double d = (x > y) ? 1.5 : 2.5;
    
    int error = condition ? x : true;  // Error: branches have incompatible types
    int error2 = x ? 1 : 2;           // Error: condition is not boolean
}
```
## 10. Type Conversion
```
{
    int x = 5;
    double d = 3.14;
    bool b = true;
    string s = "hi";
    
    double d1 = (double)x;   // Convert int to double
    int x1 = (int)d;          // Convert double to int
    string s1 = (string)x;    // Convert int to string
    
    int error = (int)b;      // Error: can't convert bool to int
    bool error2 = (bool) x;
    int error3 = (int) s;    // Error: can't convert string to int
    double error4 = (double) s;
    
}
```