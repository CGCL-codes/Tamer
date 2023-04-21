    public final InputStream getMessageAsStream(final int index) throws IOException {
        ByteBuffer buffer = null;
        if (CapabilityHints.isHintEnabled(CapabilityHints.KEY_MBOX_CACHE_BUFFERS)) {
            buffer = retrieveBufferFromCache(index);
        }
        if (buffer == null) {
            long position = getMessagePositions()[index];
            long size;
            if (index < getMessagePositions().length - 1) {
                size = getMessagePositions()[index + 1] - getMessagePositions()[index];
            } else {
                size = getChannel().size() - getMessagePositions()[index];
            }
            buffer = read(position, (int) size);
            if (CapabilityHints.isHintEnabled(CapabilityHints.KEY_MBOX_CACHE_BUFFERS)) {
                putBufferInCache(index, buffer);
            }
        }
        return new MessageInputStream(buffer);
    }
