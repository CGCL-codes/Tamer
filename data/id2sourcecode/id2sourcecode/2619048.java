    public void transferByteRangeTo(Output output, boolean utf8String, int fieldNumber, boolean repeated) throws IOException {
        final int size = readRawVarint32();
        if (size <= (bufferSize - bufferPos) && size > 0) {
            output.writeByteRange(utf8String, fieldNumber, buffer, bufferPos, size, repeated);
            bufferPos += size;
        } else {
            output.writeByteRange(utf8String, fieldNumber, readRawBytes(size), 0, size, repeated);
        }
    }
