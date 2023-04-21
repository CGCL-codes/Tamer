    private int convertSamples(IAudioSamples samples, byte[] buffer, int length) {
        if (samples.getFormat() != IAudioSamples.Format.FMT_S16) {
            log.error("Unsupported audio samples format: " + samples.getFormat());
            return length;
        }
        if (samples.getChannels() == 2) {
            return length;
        }
        if (samples.getChannels() != 1) {
            log.error("Unsupported number of audio channels: " + samples.getChannels());
            return length;
        }
        int samplesSize = length * 2;
        if (tempBuffer == null || samplesSize > tempBuffer.length) {
            tempBuffer = new byte[samplesSize];
        }
        for (int i = samplesSize - 4, j = length - 2; i >= 0; i -= 4, j -= 2) {
            byte byte1 = buffer[j + 0];
            byte byte2 = buffer[j + 1];
            tempBuffer[i + 0] = byte1;
            tempBuffer[i + 1] = byte2;
            tempBuffer[i + 2] = byte1;
            tempBuffer[i + 3] = byte2;
        }
        return samplesSize;
    }
