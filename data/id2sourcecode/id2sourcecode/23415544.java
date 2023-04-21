    @Override
    protected void setLength(final long newLength) throws IOException {
        m_writeLock.lock();
        try {
            super.setLength(newLength);
            m_ch = file.getChannel();
            m_byteBuffer = ByteBuffer.allocate((int) newLength);
            m_ch.read(m_byteBuffer, 0);
            m_byteBuffer.position(0);
        } finally {
            m_writeLock.unlock();
        }
    }
