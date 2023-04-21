    public void RemoveZero() {
        if (m_Val == 0) return;
        while (m_Val % 10 == 0) {
            m_Val /= 10;
            m_E++;
        }
    }
