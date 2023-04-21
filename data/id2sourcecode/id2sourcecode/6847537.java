    public void init(AudioSourceListener l, AudioFormat f, int fs) {
        listener = l;
        format = f;
        frameSize = fs;
        frame = new byte[fs];
        DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, format, BUFFER_SIZE);
        bitRate = format.getSampleSizeInBits();
        int bytespersecond = (int) (f.getSampleRate() * f.getFrameSize() * f.getChannels());
        int framespersecond = bytespersecond / frameSize;
        millisecondsperframe = 1000 / framespersecond;
    }
