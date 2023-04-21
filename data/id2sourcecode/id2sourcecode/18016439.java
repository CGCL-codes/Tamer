        public float[][] normalize(byte[] pData, int pPosition, int pLength) {
            int wChannels = audioFormat.getChannels();
            int wSsib = audioFormat.getSampleSizeInBits();
            int wFrameSize = audioFormat.getFrameSize();
            for (int sp = 0; sp < sampleSize; sp++) {
                if (pPosition >= pData.length) {
                    pPosition = 0;
                }
                int cdp = 0;
                for (int ch = 0; ch < wChannels; ch++) {
                    long sm = (pData[pPosition + cdp] & 0xFF) - 128;
                    for (int bt = 8, bp = 1; bt < wSsib; bt += 8) {
                        sm += pData[pPosition + cdp + bp] << bt;
                        bp++;
                    }
                    channels[ch][sp] = (float) sm / audioSampleSize;
                    cdp += channelSize;
                }
                pPosition += wFrameSize;
            }
            return channels;
        }
