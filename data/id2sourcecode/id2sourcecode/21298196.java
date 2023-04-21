    public void run() {
        FloatSampleBuffer mixBuffer = new FloatSampleBuffer();
        if (Debug.getTracePlay()) Debug.out("PlayEngine.run(): begin");
        while (!m_bTerminate) {
            while (!m_bTerminate && !m_bPlaying) {
                if (Debug.getTracePlay()) Debug.out("PlayEngine.run(): waiting for started state");
                synchronized (m_thread) {
                    try {
                        m_thread.wait();
                    } catch (InterruptedException ie) {
                    }
                }
                if (Debug.getTracePlay()) Debug.out("PlayEngine.run(): woke up from waiting");
            }
            if (m_bTerminate) break;
            int playCounter = m_playCounter;
            reconfigureMetronome();
            int frameSize = m_format.getFrameSize();
            int bufferSampleCount = m_bufferSize / frameSize;
            AudioFormat format = m_format;
            mixBuffer.reset(format.getChannels(), bufferSampleCount, format.getSampleRate());
            byte[] byteData = new byte[m_bufferSize];
            if (Debug.getTracePlay()) Debug.out("PlayEngine.run(): start of play loop");
            while (!m_bTerminate && m_bPlaying && playCounter == m_playCounter) {
                mixBuffer.makeSilence();
                mixAll(mixBuffer);
                mixBuffer.convertToByteArray(byteData, 0, format);
                synchronized (m_lineLock) {
                    if (m_line != null) {
                        int toWrite = byteData.length;
                        int offset = 0;
                        while (!m_bTerminate && m_bPlaying && playCounter == m_playCounter && toWrite > 0) {
                            int written = m_line.write(byteData, offset, toWrite);
                            if (Debug.getTracePlay()) {
                                if (written != toWrite) {
                                    Debug.out("PlayEngine.run(): line wrote " + written + " bytes instead of requested " + toWrite);
                                }
                            }
                            toWrite -= written;
                            offset += written;
                            if (!m_lineStarted) {
                                if (Debug.getTracePlay()) Debug.out("PlayEngine.run(): started line");
                                m_line.start();
                                m_lineStarted = true;
                            }
                        }
                        m_position += (offset / frameSize);
                        m_positionCurrentTime = AudioUtils.getCurrentTime();
                    }
                }
            }
            if (Debug.getTracePlay()) Debug.out("PlayEngine.run(): end of play loop");
        }
        if (Debug.getTracePlay()) {
            Debug.out("PlayEngine.run(): terminated by the m_bTerminate flag.");
        }
    }
