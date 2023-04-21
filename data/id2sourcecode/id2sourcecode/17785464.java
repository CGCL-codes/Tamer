    private boolean isClassAttributeDeclaration(String methodName) {
        return (methodName.equals("cattr") || methodName.equals("cattr_reader") || methodName.equals("cattr_writer") || methodName.equals("cattr_accessor"));
    }
