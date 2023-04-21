    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber, boolean repeated) throws IOException {
        if (utf8String) output.writeString(fieldNumber, readString(), repeated); else output.writeByteArray(fieldNumber, readByteArray(), repeated);
    }
