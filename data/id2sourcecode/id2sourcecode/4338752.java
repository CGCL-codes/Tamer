    private static AudioFormat decodeFormat(AudioInputStream inputStream) {
        AudioFormat songFormat = inputStream.getFormat();
        Encoding enc = AudioFormat.Encoding.PCM_SIGNED;
        float samplingRate = songFormat.getSampleRate();
        int samplingSize = 16;
        int channels = songFormat.getChannels();
        int frameSize = channels * samplingSize / 8;
        float framesRate = samplingRate;
        AudioFormat decodedFormat = new AudioFormat(enc, samplingRate, samplingSize, channels, frameSize, framesRate, false);
        return decodedFormat;
    }
