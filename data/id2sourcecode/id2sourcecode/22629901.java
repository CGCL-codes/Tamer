    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        openCheck();
        if (!src.isOpen()) {
            throw new ClosedChannelException();
        }
        if (position < 0 || count < 0) {
            throw new IllegalArgumentException();
        }
        if (position > size()) {
            return 0;
        }
        ByteBuffer buffer = null;
        try {
            if (src instanceof FileChannel) {
                FileChannel fileSrc = (FileChannel) src;
                long size = fileSrc.size();
                long filePosition = fileSrc.position();
                count = Math.min(count, size - filePosition);
                buffer = fileSrc.map(MapMode.READ_ONLY, filePosition, count);
                fileSrc.position(filePosition + count);
            } else {
                buffer = ByteBuffer.allocateDirect((int) count);
                src.read(buffer);
                buffer.flip();
            }
            return write(buffer, position);
        } finally {
            if (buffer != null) {
                ((DirectBuffer) buffer).free();
            }
        }
    }
