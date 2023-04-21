    @Override
    protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
        if (methodName.matches("(write|read|(remove|replace|contains)Attribute)")) {
            return false;
        }
        return super.isOperation(methodName, paramTypes);
    }
