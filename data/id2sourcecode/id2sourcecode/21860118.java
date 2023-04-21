    public SNMPTableModel(String[] headers, Class<ProcessInfoPojo> _objClass, Functor[] readFunctors, Functor[] writeFunctors, Class<Object>[] editorClasses) {
        this(headers, readFunctors, writeFunctors, editorClasses);
        this.objectClass = _objClass;
    }
