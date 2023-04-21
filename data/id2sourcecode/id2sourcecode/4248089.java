    public static boolean isSerializationMethod(IMethod method) throws JavaModelException {
        if (!Flags.isStatic(method.getFlags())) {
            String methodName = method.getElementName();
            if (method.getNumberOfParameters() == 0) {
                return "writeReplace".equals(methodName) || "readResolve".equals(methodName) || "readObjectNoData".equals(methodName);
            } else if (method.getNumberOfParameters() == 1) {
                return "writeObject".equals(methodName) || "readObject".equals(methodName);
            }
        }
        return false;
    }
