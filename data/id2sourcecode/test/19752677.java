    private void checkStreamFormat() throws AVIException {
        AVIStreamHeader header = getStreamHeader();
        AVIAudioStreamFormat format = (AVIAudioStreamFormat) getStreamFormat();
        double duration = header.getLength() * (double) header.getScale() / (double) header.getRate();
        int samplesPerSecond = format.getSamplesPerSecond();
        int channels = format.getChannels();
        int bytesPerSecond = format.getAvgBytesPerSec();
        boolean variableBitRate;
        if (header.getSampleSize() == 0) {
            variableBitRate = true;
        } else {
            variableBitRate = false;
            if (header.getSampleSize() == 1) {
                int bitRate = (8 * bytesPerSecond + 500) / 1000;
                int bytesPerFrame = MP3Frame.getBytesPerFrame(bitRate, samplesPerSecond);
                int samplesPerFrame = MP3Frame.getSamplesPerFrame(samplesPerSecond);
                if (bytesPerSecond != (bytesPerFrame * samplesPerSecond) / samplesPerFrame) {
                    warning("Changing MP3 audio from constant (CBR) to variable bitrate (VBR) packing mode");
                    variableBitRate = true;
                }
            }
        }
        setupStreamFormat(samplesPerSecond, channels, bytesPerSecond, duration, variableBitRate, getFramesPerPacket());
    }
