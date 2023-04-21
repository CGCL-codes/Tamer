    void write(int writePtr, byte[] bytes, int offset, int len) throws IOException {
        if (bytes == null) {
            throw new NullPointerException();
        } else if ((offset < 0) || (offset > bytes.length) || (len < 0) || ((offset + len) > bytes.length) || ((offset + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        if ((long) writePtr + len > Integer.MAX_VALUE) {
            throw new IOException("BLOB may not exceed 2GB in size");
        }
        if (writePtr > length) {
            throw new IOException("BLOB buffer has been truncated");
        }
        if (raFile != null) {
            if (len >= PAGE_SIZE) {
                if (bufferDirty) {
                    writePage(currentPage);
                }
                currentPage = INVALID_PAGE;
                raFile.seek(writePtr);
                raFile.write(bytes, offset, len);
                writePtr += len;
            } else {
                int count = len;
                while (count > 0) {
                    if (currentPage != (writePtr & PAGE_MASK)) {
                        readPage(writePtr);
                    }
                    int inBuffer = Math.min(PAGE_SIZE - (writePtr & BYTE_MASK), count);
                    System.arraycopy(bytes, offset, buffer, writePtr & BYTE_MASK, inBuffer);
                    bufferDirty = true;
                    offset += inBuffer;
                    writePtr += inBuffer;
                    count -= inBuffer;
                }
            }
        } else {
            if (writePtr + len > buffer.length) {
                growBuffer(writePtr + len);
            }
            System.arraycopy(bytes, offset, buffer, writePtr, len);
            writePtr += len;
        }
        if (writePtr > length) {
            length = writePtr;
        }
    }
