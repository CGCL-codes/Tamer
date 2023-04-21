    public AudioChunk(AudioFormat audioFormat, byte[] data) {
        if (!supports(audioFormat)) {
            throw new NullPointerException();
        }
        this.audioFormat = audioFormat;
        frameSize = audioFormat.getFrameSize();
        frameRate = audioFormat.getFrameRate();
        framesPerTimeDistort = TIME_DISTORT_CHUNKSIZE_BYTES / frameSize;
        this.data = data;
        sampleSizeInBits = audioFormat.getSampleSizeInBits();
        sampleSizeInBytes = sampleSizeInBits / 8;
        encoding = audioFormat.getEncoding();
        numChannels = audioFormat.getChannels();
        littleEndian = !audioFormat.isBigEndian();
        isSignedSixteen = (encoding == AudioFormat.Encoding.PCM_SIGNED && sampleSizeInBits == 16);
        isUnsignedEight = (encoding == AudioFormat.Encoding.PCM_UNSIGNED && sampleSizeInBits == 8);
        isSignedEight = (encoding == AudioFormat.Encoding.PCM_SIGNED && sampleSizeInBits == 8);
        amplitudeRange = isSignedSixteen ? (double) 0x7fff : (double) 0x7f;
    }
