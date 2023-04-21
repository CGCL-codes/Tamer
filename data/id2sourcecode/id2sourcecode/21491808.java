    public void socketReadyForWrite() {
        try {
            deleteInterest(SelectionKey.OP_WRITE);
            if (!isOpen()) return;
            fillCurrentOutgoingBuffer();
            if (m_socketWriter.isEmpty()) {
                return;
            }
            while (!m_socketWriter.isEmpty()) {
                boolean bytesWereWritten = m_socketWriter.write(getChannel());
                if (!bytesWereWritten) {
                    addInterest(SelectionKey.OP_WRITE);
                    return;
                }
                if (m_socketWriter.isEmpty()) {
                    notifyPacketSent(m_socketWriter.getTag());
                    fillCurrentOutgoingBuffer();
                }
            }
        } catch (Exception e) {
            close(e);
        }
    }
