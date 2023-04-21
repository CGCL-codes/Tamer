    protected RrdNioByteBufferBackend(final String path, final boolean readOnly) throws IOException, IllegalStateException {
        super(path, readOnly);
        if (file != null) {
            m_ch = file.getChannel();
            m_byteBuffer = ByteBuffer.allocate((int) m_ch.size());
            m_ch.read(m_byteBuffer, 0);
        } else {
            throw new IllegalStateException("File in base class is null.");
        }
    }
