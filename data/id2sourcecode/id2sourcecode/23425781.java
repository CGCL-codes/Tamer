    public LocalVariablePattern(boolean findDeclarations, boolean readAccess, boolean writeAccess, LocalVariable localVariable, int matchRule) {
        super(LOCAL_VAR_PATTERN, findDeclarations, readAccess, writeAccess, localVariable.getElementName().toCharArray(), matchRule);
        this.localVariable = localVariable;
    }
