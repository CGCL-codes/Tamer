    public static Object packageAndInvoke(Object obj, int methodID, int argAddress, VM_Type expectReturnType, boolean skip4Args, boolean isVarArg) throws Exception {
        VM_Magic.pragmaNoOptCompile();
        VM_Magic.pragmaNoInline();
        VM_Method targetMethod;
        int returnValue;
        targetMethod = VM_MethodDictionary.getValue(methodID);
        VM_Type returnType = targetMethod.getReturnType();
        if (expectReturnType == null) {
            if (!returnType.isReferenceType()) throw new Exception("Wrong return type for method: expect reference type instead of " + returnType);
        } else {
            if (returnType != expectReturnType) throw new Exception("Wrong return type for method: expect " + expectReturnType + " instead of " + returnType);
        }
        Object[] argObjectArray;
        if (isVarArg) {
            argObjectArray = packageParameterFromVarArg(targetMethod, argAddress);
        } else {
            argObjectArray = packageParameterFromJValue(targetMethod, argAddress);
        }
        Object returnObj = VM_Reflection.invoke(targetMethod, obj, argObjectArray, skip4Args);
        return returnObj;
    }
