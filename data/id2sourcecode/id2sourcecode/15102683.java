    public void init(AudioSourceListener l, AudioFormat f, int fs) {
        listener = l;
        format = f;
        frameSize = fs;
        frame = new byte[fs];
        DataLine.Info lineInfo = new DataLine.Info(TargetDataLine.class, format, BUFFER_SIZE);
        soundFile = new File(filename);
        if (!soundFile.exists()) {
            System.err.println("Wave file not found: " + filename);
            return;
        }
        inputStream = null;
        try {
            inputStream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat ff = inputStream.getFormat();
            int bytespersecond = (int) (ff.getSampleRate() * ff.getFrameSize() * ff.getChannels());
            int framespersecond = bytespersecond / frameSize;
            millisecondsperframe = 1000 / framespersecond;
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
    }
