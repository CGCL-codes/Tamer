    private void openRecorderLine(AudioFormat fmt) {
        float sampleRate = (float) fmt.getSampleRate();
        int sampleSizeInBits = fmt.getSampleSizeInBits();
        int channels = fmt.getChannels();
        boolean bigEndian = fmt.getEndian() == 1;
        Encoding encoding = getEncoding(fmt.getEncoding());
        format = new javax.sound.sampled.AudioFormat(encoding, sampleRate, sampleSizeInBits, channels, 1, sampleRate, bigEndian);
        if (encoding == SpeexEncoding.SPEEX) {
            fileType = SPEEX;
        } else {
            fileType = AudioFileFormat.Type.WAVE;
        }
        recorderCache = new RecorderCache();
    }
