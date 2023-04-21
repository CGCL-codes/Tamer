    @Override
    protected void memcpy(int destination, int source, int length, boolean checkOverlap) {
        destination = normalizeAddress(destination);
        source = normalizeAddress(source);
        if (checkOverlap || !areOverlapping(destination, source, length)) {
            ByteBuffer destinationBuffer = getBuffer(destination, length);
            ByteBuffer sourceBuffer = getBuffer(source, length);
            destinationBuffer.put(sourceBuffer);
        } else {
            IMemoryReader sourceReader = MemoryReader.getMemoryReader(source, length, 1);
            for (int i = 0; i < length; i++) {
                write8(destination + i, (byte) sourceReader.readNext());
            }
        }
    }
