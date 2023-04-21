        public SilenceInputStream(AudioFormat audioFormat) {
            m_abOneFrameBuffer = new byte[audioFormat.getFrameSize()];
            if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
            } else if (audioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
                int nSampleSizeInBits = audioFormat.getSampleSizeInBits();
                int nSampleSizeInBytes = audioFormat.getFrameSize() / audioFormat.getChannels();
                switch(nSampleSizeInBits) {
                    case 8:
                        m_abOneFrameBuffer[0] = (byte) 0x80;
                        break;
                    case 16:
                        if (audioFormat.isBigEndian()) {
                            m_abOneFrameBuffer[0] = (byte) 0x80;
                            m_abOneFrameBuffer[1] = (byte) 0x00;
                        } else {
                            m_abOneFrameBuffer[0] = (byte) 0x00;
                            m_abOneFrameBuffer[1] = (byte) 0x80;
                        }
                        break;
                    case 24:
                        if (audioFormat.isBigEndian()) {
                            m_abOneFrameBuffer[0] = (byte) 0x80;
                            m_abOneFrameBuffer[1] = (byte) 0x00;
                            m_abOneFrameBuffer[2] = (byte) 0x00;
                        } else {
                            m_abOneFrameBuffer[0] = (byte) 0x00;
                            m_abOneFrameBuffer[1] = (byte) 0x00;
                            m_abOneFrameBuffer[2] = (byte) 0x80;
                        }
                        break;
                    case 32:
                        if (audioFormat.isBigEndian()) {
                            m_abOneFrameBuffer[0] = (byte) 0x80;
                            m_abOneFrameBuffer[1] = (byte) 0x00;
                            m_abOneFrameBuffer[2] = (byte) 0x00;
                            m_abOneFrameBuffer[3] = (byte) 0x00;
                        } else {
                            m_abOneFrameBuffer[0] = (byte) 0x00;
                            m_abOneFrameBuffer[1] = (byte) 0x00;
                            m_abOneFrameBuffer[2] = (byte) 0x00;
                            m_abOneFrameBuffer[3] = (byte) 0x80;
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("sample size not supported");
                }
                for (int i = 1; i < audioFormat.getChannels(); i++) {
                    System.arraycopy(m_abOneFrameBuffer, 0, m_abOneFrameBuffer, i * nSampleSizeInBytes, nSampleSizeInBytes);
                }
            } else {
                throw new IllegalArgumentException("encoding is not PCM_SIGNED or PCM_UNSIGNED");
            }
        }
