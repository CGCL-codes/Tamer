    @Override
    public int readAt(long position, ByteBuffer buffer) throws IOException {
        final int startBufferPosition = buffer.position();
        try {
            this.file.getChannel().read(buffer, position);
        } catch (final IOException e) {
            recordIOError(e);
            throw e;
        }
        return buffer.position() - startBufferPosition;
    }
