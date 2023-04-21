    private void checkStreamFormat() throws AVIException {
        AVIStreamHeader header = getStreamHeader();
        if (!header.isAudioType()) throw new AVIInvalidStreamTypeException();
        AVIAudioStreamFormat format = getStreamFormat();
        int sampleRate = format.getSamplesPerSecond();
        if (sampleRate != 48000 && sampleRate != 44100 && sampleRate != 32000 && sampleRate != 24000 && sampleRate != 22050 && sampleRate != 16000) warning("Audio sample rate " + sampleRate + " is not standard (48000, 44100, 32000, 24000, 22050 or 16000 Hz)");
        short channels = format.getChannels();
        if (channels != 1 && channels != 2 && channels != 4 && channels != 5 && channels != 6) {
            error("Audio streams don't support " + channels + " channels");
            throw new AVIInvalidStreamChannelsException();
        }
        if (header.getSampleSize() != 0) {
            if (header.getSampleSize() != format.getBlockAlign()) throw new AVIInvalidStreamSampleSizeException();
        }
    }
