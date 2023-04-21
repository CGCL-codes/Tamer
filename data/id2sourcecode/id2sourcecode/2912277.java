        private void decodeDataPacket() {
            int nSamples;
            if (m_vorbisBlock.synthesis(m_oggPacket) == 0) {
                m_vorbisDspState.blockIn(m_vorbisBlock);
            }
            while ((nSamples = m_vorbisDspState.pcmOut(m_aPcmOut)) > 0) {
                for (int nChannel = 0; nChannel < m_vorbisInfo.getChannels(); nChannel++) {
                    int pointer = nChannel * getSampleSizeInBytes();
                    for (int j = 0; j < nSamples; j++) {
                        float fVal = m_aPcmOut[nChannel][j];
                        clipAndWriteSample(fVal, pointer);
                        pointer += getFrameSize();
                    }
                }
                m_vorbisDspState.read(nSamples);
                getCircularBuffer().write(convbuffer, 0, getFrameSize() * nSamples);
            }
        }
