package TypeChecking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.Deque;

public class SymbolTable {
    // Stack of scopes (innermost scope is on top)
    private final Deque<Map<String, TypeCheck>> scopes = new ArrayDeque<>();

    // Functions and methods are global
    private final Map<String, FunctionSignature> functions = new HashMap<>();
    private final Map<TypeCheck, Map<String, MethodSignature>> methods = new HashMap<>();

    public SymbolTable() {
        // Always start with a global scope
        enterScope();
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
}