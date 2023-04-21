    public SignalFormat getFormat(URL url) {
        SignalFormat signalFormat = null;
        AudioInputStream in;
        try {
            signalFormat = new SignalFormat();
            in = AudioSystem.getAudioInputStream(url);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            Long size = Long.valueOf(in.getFrameLength() * baseFormat.getFrameSize());
            signalFormat.setLength(size.doubleValue());
            signalFormat.setSampleRate((double) decodedFormat.getSampleRate());
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return signalFormat;
    }
