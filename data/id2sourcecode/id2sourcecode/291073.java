    public int read(byte[] abData, int nOffset, int nLength) throws IOException {
        if (DEBUG) {
            out("MixingAudioInputStream.read(byte[], int, int): begin");
            out("MixingAudioInputStream.read(byte[], int, int): requested length: " + nLength);
        }
        if (m_audioInputStreamList.isEmpty()) {
            if (DEBUG) {
                out("MixingAudioInputStream.read(byte[], int, int): no more streams. return -1. ");
            }
            return -1;
        }
        int nChannels = getFormat().getChannels();
        int nFrameSize = getFormat().getFrameSize();
        int nSampleSize = nFrameSize / nChannels;
        boolean bBigEndian = getFormat().isBigEndian();
        AudioFormat.Encoding encoding = getFormat().getEncoding();
        if (DEBUG) {
            out("MixingAudioInputStream.read(byte[], int, int): channels: " + nChannels);
            out("MixingAudioInputStream.read(byte[], int, int): frame size: " + nFrameSize);
            out("MixingAudioInputStream.read(byte[], int, int): sample size (bytes, storage size): " + nSampleSize);
            out("MixingAudioInputStream.read(byte[], int, int): big endian: " + bBigEndian);
            out("MixingAudioInputStream.read(byte[], int, int): encoding: " + encoding);
        }
        byte[] abBuffer = new byte[nFrameSize];
        int[] anMixedSamples = new int[nChannels];
        for (int nFrameBoundry = 0; nFrameBoundry < nLength; nFrameBoundry += nFrameSize) {
            if (DEBUG) {
                out("MixingAudioInputStream.read(byte[], int, int): frame boundry: " + nFrameBoundry);
            }
            for (int i = 0; i < nChannels; i++) {
                anMixedSamples[i] = 0;
            }
            Iterator streamIterator = m_audioInputStreamList.iterator();
            while (streamIterator.hasNext()) {
                AudioInputStream stream = (AudioInputStream) streamIterator.next();
                if (DEBUG) {
                    out("MixingAudioInputStream.read(byte[], int, int): AudioInputStream: " + stream);
                }
                int nBytesRead = stream.read(abBuffer, 0, nFrameSize);
                if (DEBUG) {
                    out("MixingAudioInputStream.read(byte[], int, int): bytes read: " + nBytesRead);
                }
                if (nBytesRead == -1) {
                    streamIterator.remove();
                    continue;
                }
                for (int nChannel = 0; nChannel < nChannels; nChannel++) {
                    int nBufferOffset = nChannel * nSampleSize;
                    int nSampleToAdd = 0;
                    if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
                        switch(nSampleSize) {
                            case 1:
                                nSampleToAdd = abBuffer[nBufferOffset];
                                break;
                            case 2:
                                nSampleToAdd = TConversionTool.bytesToInt16(abBuffer, nBufferOffset, bBigEndian);
                                break;
                            case 3:
                                nSampleToAdd = TConversionTool.bytesToInt24(abBuffer, nBufferOffset, bBigEndian);
                                break;
                            case 4:
                                nSampleToAdd = TConversionTool.bytesToInt32(abBuffer, nBufferOffset, bBigEndian);
                                break;
                        }
                    } else if (encoding.equals(AudioFormat.Encoding.ALAW)) {
                        nSampleToAdd = TConversionTool.alaw2linear(abBuffer[nBufferOffset]);
                    } else if (encoding.equals(AudioFormat.Encoding.ULAW)) {
                        nSampleToAdd = TConversionTool.ulaw2linear(abBuffer[nBufferOffset]);
                    }
                    anMixedSamples[nChannel] += nSampleToAdd;
                }
            }
            if (DEBUG) {
                out("MixingAudioInputStream.read(byte[], int, int): starting to write to buffer passed by caller");
            }
            for (int nChannel = 0; nChannel < nChannels; nChannel++) {
                if (DEBUG) {
                    out("MixingAudioInputStream.read(byte[], int, int): channel: " + nChannel);
                }
                int nBufferOffset = nOffset + nFrameBoundry + nChannel * nSampleSize;
                if (DEBUG) {
                    out("MixingAudioInputStream.read(byte[], int, int): buffer offset: " + nBufferOffset);
                }
                if (encoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
                    switch(nSampleSize) {
                        case 1:
                            abData[nBufferOffset] = (byte) anMixedSamples[nChannel];
                            break;
                        case 2:
                            TConversionTool.intToBytes16(anMixedSamples[nChannel], abData, nBufferOffset, bBigEndian);
                            break;
                        case 3:
                            TConversionTool.intToBytes24(anMixedSamples[nChannel], abData, nBufferOffset, bBigEndian);
                            break;
                        case 4:
                            TConversionTool.intToBytes32(anMixedSamples[nChannel], abData, nBufferOffset, bBigEndian);
                            break;
                    }
                } else if (encoding.equals(AudioFormat.Encoding.ALAW)) {
                    abData[nBufferOffset] = TConversionTool.linear2alaw((short) anMixedSamples[nChannel]);
                } else if (encoding.equals(AudioFormat.Encoding.ULAW)) {
                    abData[nBufferOffset] = TConversionTool.linear2ulaw(anMixedSamples[nChannel]);
                }
            }
        }
        if (DEBUG) {
            out("MixingAudioInputStream.read(byte[], int, int): end");
        }
        return nLength;
    }
