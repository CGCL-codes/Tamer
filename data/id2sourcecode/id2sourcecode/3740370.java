    protected void marshalReadOnly(boolean readOnly, HierarchicalStreamWriter writer) {
        writer.startNode("readOnly");
        writer.setValue(String.valueOf(readOnly));
        writer.endNode();
    }
