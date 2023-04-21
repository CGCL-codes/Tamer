    private void mapFile() throws IOException {
        final long length = getLength();
        if (length > 0) {
            final FileChannel.MapMode mapMode = isReadOnly() ? FileChannel.MapMode.READ_ONLY : FileChannel.MapMode.READ_WRITE;
            m_byteBuffer = file.getChannel().map(mapMode, 0, length);
        }
    }
