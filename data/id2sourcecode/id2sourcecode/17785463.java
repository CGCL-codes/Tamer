    private boolean isInstanceAttributeDeclaration(String methodName) {
        return (methodName.equals("attr") || methodName.equals("attr_reader") || methodName.equals("attr_writer") || methodName.equals("attr_accessor"));
    }
