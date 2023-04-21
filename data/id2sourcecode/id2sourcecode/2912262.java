        public void execute() {
            if (TDebug.TraceAudioConverter) {
                TDebug.out(">EncodedVorbisAudioInputStream.execute(): begin");
            }
            int nFrameSize = getFrameSize();
            int nChannels = getChannels();
            boolean bBigEndian = isBigEndian();
            int nBytesPerSample = nFrameSize / nChannels;
            int nSampleSizeInBits = nBytesPerSample * 8;
            float fScale = (float) Math.pow(2.0, nSampleSizeInBits - 1);
            if (TDebug.TraceAudioConverter) {
                TDebug.out("frame size: " + nFrameSize);
                TDebug.out("channels: " + nChannels);
                TDebug.out("big endian: " + bBigEndian);
                TDebug.out("sample size (bits): " + nSampleSizeInBits);
                TDebug.out("bytes per sample: " + nBytesPerSample);
                TDebug.out("scale: " + fScale);
            }
            while (!eos && writeMore()) {
                if (TDebug.TraceAudioConverter) {
                    TDebug.out("writeMore(): " + writeMore());
                }
                int bytes;
                try {
                    bytes = m_decodedStream.read(m_abReadbuffer);
                    if (TDebug.TraceAudioConverter) {
                        TDebug.out("read from PCM stream: " + bytes);
                    }
                } catch (IOException e) {
                    if (TDebug.TraceAllExceptions || TDebug.TraceAudioConverter) {
                        TDebug.out(e);
                    }
                    m_streamState.clear();
                    m_block.clear();
                    m_dspState.clear();
                    m_comment.clear();
                    m_info.clear();
                    try {
                        close();
                    } catch (IOException e1) {
                        if (TDebug.TraceAllExceptions || TDebug.TraceAudioConverter) {
                            TDebug.out(e1);
                        }
                    }
                    if (TDebug.TraceAudioConverter) {
                        TDebug.out("<");
                    }
                    return;
                }
                if (bytes == 0 || bytes == -1) {
                    if (TDebug.TraceAudioConverter) {
                        TDebug.out("EOS reached; calling DspState.write(0)");
                    }
                    m_dspState.write(null, 0);
                } else {
                    int nFrames = bytes / nFrameSize;
                    if (TDebug.TraceAudioConverter) {
                        TDebug.out("processing frames: " + nFrames);
                    }
                    float[][] buffer = new float[nChannels][READ];
                    for (int i = 0; i < nFrames; i++) {
                        for (int nChannel = 0; nChannel < nChannels; nChannel++) {
                            int nSample;
                            nSample = bytesToInt16(m_abReadbuffer, i * nFrameSize + nChannel * nBytesPerSample, bBigEndian);
                            buffer[nChannel][i] = nSample / fScale;
                        }
                    }
                    m_dspState.write(buffer, nFrames);
                }
                while (m_dspState.blockOut(m_block) == 1) {
                    m_block.analysis(null);
                    m_block.addBlock();
                    while (m_dspState.flushPacket(m_packet) != 0) {
                        m_streamState.packetIn(m_packet);
                        while (!eos) {
                            int result = m_streamState.pageOut(m_page);
                            if (result == 0) {
                                break;
                            }
                            getCircularBuffer().write(m_page.getHeader());
                            getCircularBuffer().write(m_page.getBody());
                            if (m_page.isEos()) {
                                eos = true;
                                if (TDebug.TraceAudioConverter) {
                                    TDebug.out("page has detected EOS");
                                }
                            }
                        }
                    }
                }
            }
            if (eos) {
                if (TDebug.TraceAudioConverter) {
                    TDebug.out("EOS; shutting down encoder");
                }
                m_streamState.clear();
                m_block.clear();
                m_dspState.clear();
                m_comment.clear();
                m_info.clear();
                getCircularBuffer().close();
                try {
                    close();
                } catch (IOException e) {
                    if (TDebug.TraceAllExceptions || TDebug.TraceAudioConverter) {
                        TDebug.out(e);
                    }
                }
            }
            if (TDebug.TraceAudioConverter) {
                TDebug.out("<EncodedVorbisAudioInputStream.execute(): end");
            }
        }
