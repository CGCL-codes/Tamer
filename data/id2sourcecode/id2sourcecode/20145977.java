    private ByteBuffer read(final long position, final int size) throws IOException {
        ByteBuffer buffer = null;
        try {
            BufferStrategy bufferStrategy = null;
            if (Configurator.getProperty(KEY_BUFFER_STRATEGY) != null) {
                bufferStrategy = BufferStrategy.valueOf(Configurator.getProperty(KEY_BUFFER_STRATEGY).toUpperCase());
            }
            if (BufferStrategy.MAPPED.equals(bufferStrategy)) {
                buffer = getChannel().map(FileChannel.MapMode.READ_ONLY, position, size);
            } else {
                if (BufferStrategy.DIRECT.equals(bufferStrategy)) {
                    buffer = ByteBuffer.allocateDirect(size);
                } else if (BufferStrategy.DEFAULT.equals(bufferStrategy) || bufferStrategy == null) {
                    buffer = ByteBuffer.allocate(size);
                } else {
                    throw new IllegalArgumentException("Unrecognised buffer strategy: " + Configurator.getProperty(KEY_BUFFER_STRATEGY));
                }
                getChannel().position(position);
                getChannel().read(buffer);
                buffer.flip();
            }
        } catch (IOException ioe) {
            log.warn("Error reading bytes using nio", ioe);
            getRaf().seek(position);
            byte[] buf = new byte[size];
            getRaf().read(buf);
            buffer = ByteBuffer.wrap(buf);
        }
        return buffer;
    }
