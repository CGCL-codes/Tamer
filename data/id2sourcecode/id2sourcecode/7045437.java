    public AudioFileBitReader(File file) throws IOException {
        try {
            ais = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException e) {
            throw new IOException(e.toString());
        }
        AudioFormat format = ais.getFormat();
        format = new AudioFormat(format.getEncoding(), format.getSampleRate(), format.getSampleSizeInBits(), format.getChannels(), format.getFrameSize(), format.getFrameRate(), true);
        System.out.println("length = " + ais.getFrameLength() + " samples, format: " + format);
        frameSize = format.getFrameSize();
    }
