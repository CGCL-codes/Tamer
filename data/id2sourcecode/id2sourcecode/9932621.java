    private void recycleBuffer() {
        if (readIndex <= 0) {
            return;
        }
        log.info("Recycling ByteBuffer");
        int indexAdjustment = readIndex;
        int recycleIndex = 0;
        while (writeIndex - readIndex > tempbuffer.length) {
            buffer.position(readIndex);
            buffer.get(tempbuffer, 0, tempbuffer.length);
            buffer.position(recycleIndex);
            buffer.put(tempbuffer, 0, tempbuffer.length);
            recycleIndex += tempbuffer.length;
            readIndex += tempbuffer.length;
        }
        if (writeIndex - readIndex > 0) {
            buffer.position(readIndex);
            buffer.get(tempbuffer, 0, writeIndex - readIndex);
            buffer.position(recycleIndex);
            buffer.put(tempbuffer, 0, writeIndex - readIndex);
        }
        indexOffset += indexAdjustment;
        writeIndex -= indexAdjustment;
        readIndex = 0;
    }
