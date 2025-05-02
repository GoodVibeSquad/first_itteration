## 1. Test Shift & Reduce From TableGenerator
```
//input
7;         

//7 should reduce to Slist[SExpression(Econstant(CInt(7)))]
//if it does we can assume that it shifts and reduces

```
## 2. Testing constants
```
7;
3.14;
true;
"test"
Pi;
Euler;      
```

## 3. Testing Expressions
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

Print(x) //should produce an error
5%;      // should produce an error 

```

## 4. Testing Statements
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

## 5. Testing Identifiers
```
 x = 7;
 int y = 12;
 
 type keyword; // should produce error
```






