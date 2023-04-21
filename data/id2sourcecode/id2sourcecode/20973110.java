    protected void transferCollectionId(Input input, Output output, int fieldNumber) throws IOException {
        output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }
