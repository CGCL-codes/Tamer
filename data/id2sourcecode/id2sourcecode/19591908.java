    public static AudioFormat convertFormat(javax.sound.sampled.AudioFormat format) {
        Encoding encoding = format.getEncoding();
        int channels = format.getChannels();
        float frameRate = format.getFrameRate();
        int frameSize = format.getFrameSize() < 0 ? format.getFrameSize() : (format.getFrameSize() * 8);
        float sampleRate = format.getSampleRate();
        int sampleSize = format.getSampleSizeInBits();
        int endian = format.isBigEndian() ? AudioFormat.BIG_ENDIAN : AudioFormat.LITTLE_ENDIAN;
        int signed = Format.NOT_SPECIFIED;
        String encodingString = AudioFormat.LINEAR;
        if (encoding == Encoding.PCM_SIGNED) {
            signed = AudioFormat.SIGNED;
            encodingString = AudioFormat.LINEAR;
        } else if (encoding == Encoding.PCM_UNSIGNED) {
            signed = AudioFormat.UNSIGNED;
            encodingString = AudioFormat.LINEAR;
        } else if (encoding == Encoding.ALAW) {
            encodingString = AudioFormat.ALAW;
        } else if (encoding == Encoding.ULAW) {
            encodingString = AudioFormat.ULAW;
        } else {
            encodingString = encoding.toString();
        }
        AudioFormat jmfFormat = new AudioFormat(encodingString, sampleRate, sampleSize, channels, endian, signed, frameSize, frameRate, Format.byteArray);
        return jmfFormat;
    }
