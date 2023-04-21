    public Type getGenericType() {
        return reader == null ? writer.getGenericParameterTypes()[0] : reader.getGenericReturnType();
    }
