    final void write(int writePtr, final ByteBuffer src) throws IOException {
        this.fileHolder.ensureOpen();
        if (src == null) throw new NullPointerException();
        final int remaining = src.remaining();
        if (remaining == 0) return;
        if ((long) writePtr + remaining > Integer.MAX_VALUE) throw new IOException("BLOB may not exceed 2GB in size");
        if (writePtr > this.length) {
            throw new IOException("BLOB buffer has been truncated");
        }
        if (this.fileHolder.file != null) {
            if (remaining >= PAGE_SIZE) {
                if (this.bufferDirty) writePage(this.currentPage);
                this.currentPage = INVALID_PAGE;
                this.fileHolder.file.seek(writePtr);
                while (src.hasRemaining()) this.fileHolder.file.getChannel().write(src);
                writePtr += remaining;
            } else {
                while (src.hasRemaining()) {
                    if (this.currentPage != (writePtr & PAGE_MASK)) readPage(writePtr);
                    final int inBuffer = Math.min(PAGE_SIZE - (writePtr & BYTE_MASK), src.remaining());
                    src.get(this.buffer, writePtr & BYTE_MASK, inBuffer);
                    this.bufferDirty = true;
                    writePtr += inBuffer;
                }
            }
        } else {
            if (writePtr + remaining > this.buffer.length) growBuffer(writePtr + remaining);
            src.get(this.buffer, writePtr, remaining);
            writePtr += remaining;
        }
        if (writePtr > this.length) this.length = writePtr;
    }
