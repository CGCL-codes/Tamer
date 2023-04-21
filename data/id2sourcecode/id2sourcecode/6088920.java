    private void shiftFrame(byte b) {
        for (int i = 0; i < m_frame.length - 1; i++) {
            m_frame[i] = m_frame[i + 1];
        }
        m_frame[m_frame.length - 1] = b;
    }
