    public ADPCMHelper(InputStream audioFile, int framesPerSecond) throws IOException, UnsupportedAudioFileException {
        audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(audioFile));
        AudioFormat format = audioIn.getFormat();
        int frameSize = format.getFrameSize();
        isStereo = format.getChannels() == 2;
        is16Bit = format.getSampleSizeInBits() > 8;
        sampleRate = (int) format.getSampleRate();
        isSigned = format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;
        if (sampleRate >= 44000) sampleRate = 44000; else if (sampleRate >= 22000) sampleRate = 22000; else if (sampleRate >= 11000) sampleRate = 11000; else sampleRate = 5500;
        rate = SWFConstants.SOUND_FREQ_5_5KHZ;
        if (sampleRate == 44000) rate = SWFConstants.SOUND_FREQ_44KHZ; else if (sampleRate == 22000) rate = SWFConstants.SOUND_FREQ_22KHZ; else if (sampleRate == 11000) rate = SWFConstants.SOUND_FREQ_11KHZ;
        samplesPerFrame = sampleRate / framesPerSecond;
        FramedInputStream frameIn = new FramedInputStream(audioIn, frameSize);
        leftEncoder = new ADPCMEncodeStream(frameIn, is16Bit, isSigned);
        if (isStereo) {
            rightEncoder = new ADPCMEncodeStream(frameIn, is16Bit, isSigned);
        }
    }
