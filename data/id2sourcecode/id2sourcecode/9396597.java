    protected double backDelay(double[] pattern) {
        int y;
        for (y = 0; y < m_taps; ++y) {
            backmemory[y] = backmemory[y + 1];
            backmemory[y] += pattern[y];
        }
        backmemory[m_taps] = pattern[m_taps];
        return backmemory[0];
    }
