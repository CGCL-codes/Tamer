    protected void replaceFieldAccess(Set<CtClass> classes, String fieldName, String reader, String writer) throws CannotCompileException {
        checkForEmptySet(classes, "replaceFieldAccess()");
        Editor editor = new Editor(fieldName, reader, writer);
        runEditor(classes, editor);
    }
