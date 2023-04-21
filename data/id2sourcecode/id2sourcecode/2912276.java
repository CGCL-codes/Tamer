        private void setupVorbisStructures() {
            convsize = BUFFER_SIZE / m_vorbisInfo.getChannels();
            m_vorbisDspState.initSynthesis(m_vorbisInfo);
            m_vorbisBlock.init(m_vorbisDspState);
            m_aPcmOut = new float[m_vorbisInfo.getChannels()][];
        }
