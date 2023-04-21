    private void outputOpen() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioFormat format = getTracks().get(0).getSource().getStream().getFormat();
        DataLine.Info info;
        format = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), format.isBigEndian());
        info = new DataLine.Info(SourceDataLine.class, format);
        output = (SourceDataLine) AudioSystem.getMixer(getMixer()).getLine(info);
        output.open(format);
        output.start();
    }
