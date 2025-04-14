package TypeChecking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final Map<String, TypeCheck> table = new HashMap<>();
    private final Map<String, FunctionSignature> functions = new HashMap<>();
    private final Map<TypeCheck, Map<String, MethodSignature>> methods = new HashMap<>();

    public void declareVariable(String name, TypeCheck type) {
        table.put(name, type);
    }

    public TypeCheck lookup(String name) {
        return table.getOrDefault(name, TypeCheck.ERROR);
    }

    public boolean contains(String name) {
        return table.containsKey(name);
    }


    public void clear() {
        table.clear();
    }

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
