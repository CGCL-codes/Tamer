    protected final boolean isSerializationMethod(IMethodBinding method, boolean checkType) {
        if (null == method) {
            throw abort();
        }
        final String[] magicMethods = new String[] { "writeObject(Ljava/io/ObjectOutputStream;)V", "readObject(Ljava/io/ObjectInputStream;)V", "readObjectNoData()V", "writeReplace()Ljava/lang/Object;", "readResolve()Ljava/lang/Object;" };
        if (RuleUtils.isSignatureEqual(method, magicMethods)) {
            if (!checkType) {
                return true;
            }
            ITypeBinding serializable = resolveType("Ljava/io/Serializable;");
            if (method.getDeclaringClass().isAssignmentCompatible(serializable)) {
                return true;
            }
        }
        return false;
    }
