    public void read(DataInput dataInput) throws IOException {
        size = IntegerConverter.DEFAULT_INSTANCE.readInt(dataInput);
        bytes = IntegerConverter.DEFAULT_INSTANCE.readInt(dataInput);
        readBlockId = BooleanConverter.DEFAULT_INSTANCE.readBoolean(dataInput) ? container.objectIdConverter().read(dataInput) : null;
        readBlockOffset = IntegerConverter.DEFAULT_INSTANCE.readInt(dataInput);
        writeBlockId = BooleanConverter.DEFAULT_INSTANCE.readBoolean(dataInput) ? container.objectIdConverter().read(dataInput) : null;
        writeBlockOffset = IntegerConverter.DEFAULT_INSTANCE.readInt(dataInput);
    }
