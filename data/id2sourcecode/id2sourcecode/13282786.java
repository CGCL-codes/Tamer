    public WavStreamHandler(File fileIn) {
        fileHdl = fileIn;
        try {
            str = AudioSystem.getAudioInputStream(fileIn);
            fmt = str.getFormat();
            format = fmt.getEncoding();
            numChannels = fmt.getChannels();
            bitsPerSample = fmt.getSampleSizeInBits();
            swapBytes = !fmt.isBigEndian();
            sampleRate = (int) fmt.getSampleRate();
            frameLength = str.getFrameLength();
            buffer = ByteBuffer.allocate(BUFFERSIZE);
        } catch (Exception ex) {
        }
    }
