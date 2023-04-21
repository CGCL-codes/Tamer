    @Override
    public long transferFrom(ReadableByteChannel src, final long position, final long count) throws IOException {
        if (count <= 0) return 0;
        final int bufferLimit = this.buffer.limit();
        final int argLimit = (int) (position + count);
        if (position > bufferLimit) return 0; else if (position == bufferLimit) {
            prepareWrite(Math.min(argLimit - this.buffer.position(), this.buffer.capacity()));
            this.buffer.limit(bufferLimit);
        }
        final ByteBuffer viewBuffer = this.buffer.duplicate();
        viewBuffer.limit(Math.min(argLimit, viewBuffer.capacity()));
        viewBuffer.position((int) position);
        src.read(viewBuffer);
        if (viewBuffer.position() > bufferLimit) this.buffer.limit(viewBuffer.position());
        return viewBuffer.position() - position;
    }
