package TypeChecking;

import java.util.*;

public class SymbolTable {
    // Stack of scopes (innermost scope is on top)
    private final Stack<Map<String, TypeCheck>> scopes = new Stack<>();

    // Functions and methods are global
    private final Map<String, FunctionSignature> functions = new HashMap<>();
    private final Map<TypeCheck, Map<String, MethodSignature>> methods = new HashMap<>();

    private final Map<String, TypeCheck> types = new HashMap<>();
    private final Map<TypeCheck, List<List<TypeCheck>>> constructors = new HashMap<>();



    public SymbolTable() {
        // Always start with a global scope
        enterScope();
        registerNativeMethods();
        registerNativeType();
    }

    // Scope handling
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        if (scopes.isEmpty()) {
            throw new IllegalStateException("Cannot exit scope: no scope to exit.");
        }
        scopes.pop();
    }

    // Variable declarations
    public void declareVariable(String name, TypeCheck type) {
        if (scopes.isEmpty()) {
            throw new IllegalStateException("No scope to declare variable in.");
        }
        scopes.peek().put(name, type);
    }


    // New method to check only current scope
    public boolean declaredInScope(String name) {
        if (scopes.isEmpty()) {
            throw new IllegalStateException("No scopes available.");
        }
        return scopes.peek().containsKey(name);
    }



    public boolean contains(String name) {
        for (Map<String, TypeCheck> scope : scopes) {
            if (scope.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    public TypeCheck lookup(String name) {
        for (Map<String, TypeCheck> scope : scopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return TypeCheck.ERROR;
    }

    public void clear() {
        scopes.clear();
        enterScope(); // Re-initialize with a new global scope
    }

    // Function declarations
    public static class FunctionSignature {
        public final List<TypeCheck> paramTypes;
        public final TypeCheck returnType;

        public FunctionSignature(List<TypeCheck> paramTypes, TypeCheck returnType) {
            this.paramTypes = paramTypes;
            this.returnType = returnType;
        }
    }

    public void declareFunction(String name, List<TypeCheck> paramTypes, TypeCheck returnType) {
        functions.put(name, new FunctionSignature(paramTypes, returnType));
    }

    public boolean isFunction(String name) {
        return functions.containsKey(name);
    }

    public FunctionSignature getFunction(String name) {
        return functions.get(name);
    }

    // Method declarations
    public static class MethodSignature {
        public final List<TypeCheck> paramTypes;
        public final TypeCheck returnType;

        public MethodSignature(List<TypeCheck> paramTypes, TypeCheck returnType) {
            this.paramTypes = paramTypes;
            this.returnType = returnType;
        }
    }


    public void declareMethod(TypeCheck type, String methodName, List<TypeCheck> paramTypes, TypeCheck returnType) {
        methods.putIfAbsent(type, new HashMap<>());
        methods.get(type).put(methodName, new MethodSignature(paramTypes, returnType));
    }

    public boolean hasMethod(TypeCheck type, String methodName) {
        return methods.containsKey(type) && methods.get(type).containsKey(methodName);
    }

    public MethodSignature getMethod(TypeCheck type, String methodName) {
        return methods.get(type).get(methodName);
    }

    public void declareType(String typeName, TypeCheck type) {
        types.put(typeName, type);
    }

    public void declareConstructor(TypeCheck type, List<TypeCheck> paramTypes) {
        constructors.putIfAbsent(type, new ArrayList<>());
        constructors.get(type).add(paramTypes);
    }

    public List<List<TypeCheck>> getAllConstructors(TypeCheck type) {
        return constructors.getOrDefault(type, List.of());
    }

    public Map<String, TypeCheck> getTypes() {
        return types;
    }

    public Boolean hasType(String key){
        return types.containsKey(key);
    }

    private void registerNativeType() {
        declareType("NeuralNetwork", TypeCheck.NEURALNETWORK);
        declareType("Layer", TypeCheck.LAYER);
        declareType("ActivationFunction", TypeCheck.ACTIVATIONFUNC);


        declareConstructor(TypeCheck.NEURALNETWORK,
                List.of(TypeCheck.LAYER, TypeCheck.LAYER, TypeCheck.LAYER));

        declareConstructor(TypeCheck.NEURALNETWORK,
                List.of(TypeCheck.STRING));

        declareConstructor(TypeCheck.LAYER,
                List.of(TypeCheck.INT));

        declareConstructor(TypeCheck.LAYER,
                List.of(TypeCheck.INT, TypeCheck.STRING));

        declareConstructor(TypeCheck.LAYER,
                List.of(TypeCheck.INT, TypeCheck.INT, TypeCheck.STRING));
    }

    private void registerNativeMethods() {

        declareMethod(TypeCheck.NEURALNETWORK, "train",
                List.of(TypeCheck.STRING, TypeCheck.STRING, TypeCheck.INT, TypeCheck.INT, TypeCheck.DOUBLE),
                TypeCheck.VOID
        );

        declareMethod(TypeCheck.NEURALNETWORK, "save",
                List.of(TypeCheck.STRING),
                TypeCheck.VOID
        );

        declareMethod(TypeCheck.NEURALNETWORK, "predict",
                List.of(TypeCheck.STRING, TypeCheck.STRING),
                TypeCheck.INT
        );

        declareMethod(TypeCheck.NEURALNETWORK, "load",
                List.of(TypeCheck.STRING),
                TypeCheck.VOID
        );

        declareMethod(TypeCheck.ACTIVATIONFUNC,"Relu",
                List.of(TypeCheck.DOUBLE),
                TypeCheck.DOUBLE
        );

        declareMethod(TypeCheck.ACTIVATIONFUNC,"Softmax",
                List.of(TypeCheck.DOUBLE),
                TypeCheck.DOUBLE
        );

    }
}