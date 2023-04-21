    public Audio(String filename, String name) throws StellariumException {
        trackName = name;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename));
            AudioFormat format = stream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(format, stream);
            }
            DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
            track = (Clip) AudioSystem.getLine(info);
            track.open(stream);
        } catch (Exception e) {
            throw new StellariumException("Could not read audio clip", e);
        }
    }
