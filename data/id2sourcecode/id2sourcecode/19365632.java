    final void write(int writePtr, final byte[] bytes, int offset, final int len) throws IOException {
        this.fileHolder.ensureOpen();
        if (bytes == null) throw new NullPointerException(); else if ((offset < 0) || (offset > bytes.length) || (len < 0) || ((offset + len) > bytes.length) || ((offset + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        if ((long) writePtr + len > (long) Integer.MAX_VALUE) throw new IOException("BLOB may not exceed 2GB in size");
        if (writePtr > this.length) {
            throw new IOException("BLOB buffer has been truncated");
        }
        if (this.fileHolder.file != null) {
            if (len >= PAGE_SIZE) {
                if (bufferDirty) writePage(this.currentPage);
                this.currentPage = INVALID_PAGE;
                this.fileHolder.file.seek(writePtr);
                this.fileHolder.file.write(bytes, offset, len);
                writePtr += len;
            } else {
                int count = len;
                while (count > 0) {
                    if (this.currentPage != (writePtr & PAGE_MASK)) readPage(writePtr);
                    final int inBuffer = Math.min(PAGE_SIZE - (writePtr & BYTE_MASK), count);
                    System.arraycopy(bytes, offset, this.buffer, writePtr & BYTE_MASK, inBuffer);
                    this.bufferDirty = true;
                    offset += inBuffer;
                    writePtr += inBuffer;
                    count -= inBuffer;
                }
            }
        } else {
            if (writePtr + len > this.buffer.length) growBuffer(writePtr + len);
            System.arraycopy(bytes, offset, this.buffer, writePtr, len);
            writePtr += len;
        }
        if (writePtr > this.length) this.length = writePtr;
    }
