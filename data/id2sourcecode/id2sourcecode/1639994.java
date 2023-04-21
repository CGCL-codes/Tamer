    public void setData(FloatSampleBuffer fsb, int start, int length) {
        m_anDisplayData = new float[fsb.getChannelCount()][m_nWidth];
        calculate(fsb, m_anDisplayData, start, length);
        repaint();
    }
