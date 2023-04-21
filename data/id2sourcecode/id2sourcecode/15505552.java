    public void setData(FloatSampleBuffer fsb) {
        int length = 0;
        if (m_nWidth > 0 && fsb != null) {
            if (fsb.getChannelCount() > 0) {
                length = fsb.getSampleCount();
            }
        }
        if (length > 0) {
            int nSamplesPerPixel = length / m_nWidth;
            float data[] = fsb.getChannel(0);
            for (int i = 0; i < m_nWidth; i++) {
                float value = 0;
                for (int j = 0; j < nSamplesPerPixel; j++) {
                    value += Math.abs(data[i * nSamplesPerPixel + j]);
                }
                value /= nSamplesPerPixel;
                m_anDisplayData[i] = value;
            }
        } else {
            for (int i = 0; i < m_nWidth; i++) {
                m_anDisplayData[i] = 0.0f;
            }
        }
        repaint();
    }
