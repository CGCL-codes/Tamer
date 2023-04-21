    final void write(final int writePtr, final int b) throws IOException {
        this.fileHolder.ensureOpen();
        if (writePtr >= this.length) {
            if (writePtr > this.length) {
                throw new IOException("BLOB buffer has been truncated");
            }
            if (++this.length < 0) {
                throw new IOException("BLOB may not exceed 2GB in size");
            }
        }
        if (this.fileHolder.file != null) {
            if (this.currentPage != (writePtr & PAGE_MASK)) readPage(writePtr);
            this.buffer[writePtr & BYTE_MASK] = (byte) b;
            this.bufferDirty = true;
        } else {
            if (writePtr >= this.buffer.length) growBuffer(writePtr + 1);
            this.buffer[writePtr] = (byte) b;
        }
    }
