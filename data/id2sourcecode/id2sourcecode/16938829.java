    public void write(DataOutput dataOutput) throws IOException {
        IntegerConverter.DEFAULT_INSTANCE.writeInt(dataOutput, size);
        IntegerConverter.DEFAULT_INSTANCE.writeInt(dataOutput, bytes);
        BooleanConverter.DEFAULT_INSTANCE.writeBoolean(dataOutput, readBlockId != null);
        if (readBlockId != null) container.objectIdConverter().write(dataOutput, readBlockId);
        IntegerConverter.DEFAULT_INSTANCE.writeInt(dataOutput, readBlockOffset);
        BooleanConverter.DEFAULT_INSTANCE.writeBoolean(dataOutput, writeBlockId != null);
        if (writeBlockId != null) container.objectIdConverter().write(dataOutput, writeBlockId);
        IntegerConverter.DEFAULT_INSTANCE.writeInt(dataOutput, writeBlockOffset);
    }
