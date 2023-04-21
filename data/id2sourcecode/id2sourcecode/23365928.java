    protected void transferClassId(Input input, Output output, int fieldNumber, boolean mapped, boolean array) throws IOException {
        if (mapped) input.transferByteRangeTo(output, true, fieldNumber, false); else output.writeUInt32(fieldNumber, input.readUInt32(), false);
    }
