    public VariablePattern(int patternKind, boolean findDeclarations, boolean readAccess, boolean writeAccess, char[] name, int matchRule) {
        super(patternKind, matchRule);
        this.findDeclarations = findDeclarations;
        this.readAccess = readAccess;
        this.writeAccess = writeAccess;
        this.findReferences = readAccess || writeAccess;
        this.name = (isCaseSensitive() || isCamelCase()) ? name : CharOperation.toLowerCase(name);
    }
