    void write(int writePtr, int b) throws IOException {
        if (writePtr >= length) {
            if (writePtr > length) {
                throw new IOException("BLOB buffer has been truncated");
            }
            if (++length < 0) {
                throw new IOException("BLOB may not exceed 2GB in size");
            }
        }
        if (raFile != null) {
            if (currentPage != (writePtr & PAGE_MASK)) {
                readPage(writePtr);
            }
            buffer[writePtr & BYTE_MASK] = (byte) b;
            bufferDirty = true;
        } else {
            if (writePtr >= buffer.length) {
                growBuffer(writePtr + 1);
            }
            buffer[writePtr] = (byte) b;
        }
    }
