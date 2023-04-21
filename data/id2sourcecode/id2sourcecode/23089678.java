    public Format[] getSupportedOutputFormats(Format format) {
        if (format == null || !(format instanceof AudioFormat)) {
            return new Format[0];
        }
        AudioFormat inputFormat = (AudioFormat) format;
        JffmpegAudioFormat audioCodec = codecManager.getAudioCodec(format.getEncoding());
        return new AudioFormat[] { new AudioFormat("LINEAR", inputFormat.getSampleRate(), inputFormat.getSampleSizeInBits() > 0 ? inputFormat.getSampleSizeInBits() : 16, inputFormat.getChannels(), 0, 1) };
    }
