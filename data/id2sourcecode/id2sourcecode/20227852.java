    @Override
    public Class getBaseType() {
        return reader == null ? writer.getBaseType() : reader.getBaseType();
    }
