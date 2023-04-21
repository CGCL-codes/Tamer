    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (!m_destroyThread) {
            if (!m_recording) {
                synchronized (m_thread) {
                    try {
                        m_thread.wait();
                    } catch (InterruptedException ie) {
                    }
                }
            }
            if (m_destroyThread) break;
            if (m_recording) {
                int total = 0;
                int frameSize = m_ais.getFormat().getFrameSize();
                int floatOffset = 0;
                m_lastRecordedBuffer.reset(m_ais.getFormat().getChannels(), m_recordSampleCount, m_ais.getFormat().getSampleRate());
                try {
                    while (m_recording && !m_destroyThread) {
                        int thisRead = m_ais.read(buffer) / frameSize;
                        if (m_destroyThread) break;
                        if (thisRead > 0) {
                            if (total + thisRead >= m_recordDelay) {
                                int srcOffset = 0;
                                int sampleCount = thisRead;
                                if (total < m_recordDelay) {
                                    srcOffset = m_recordDelay - total;
                                    sampleCount -= srcOffset;
                                }
                                if (floatOffset + sampleCount > m_lastRecordedBuffer.getSampleCount()) {
                                    sampleCount = m_lastRecordedBuffer.getSampleCount() - floatOffset;
                                }
                                m_lastRecordedBuffer.setSamplesFromBytes(buffer, srcOffset, m_ais.getFormat(), floatOffset, sampleCount);
                                floatOffset += sampleCount;
                                displayTime((int) AudioUtils.samples2millis(floatOffset, m_ais.getFormat().getSampleRate()));
                            } else {
                                displayTime((int) AudioUtils.samples2millis(total + thisRead - m_recordDelay, m_ais.getFormat().getSampleRate()));
                            }
                            total += thisRead;
                            if (floatOffset >= m_lastRecordedBuffer.getSampleCount()) {
                                m_recording = false;
                                onRecordingFinished();
                            }
                        }
                    }
                } catch (Exception e) {
                    if (Debug.getTraceAllExceptions()) {
                        Debug.out(e);
                    }
                    m_timeDisplay.setText(e.getMessage());
                    m_recording = false;
                    onRecordingFinished();
                }
            }
        }
    }
