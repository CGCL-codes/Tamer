    public AmplitudeAudioInputStream getStream() throws IOException, UnsupportedAudioFileException {
        if (stream == null) {
            AudioInputStream streamInput = null;
            if (file != null) {
                streamInput = AudioSystem.getAudioInputStream(file);
            } else if (url != null) {
                streamInput = AudioSystem.getAudioInputStream(url);
            } else {
                throw new IOException("url/source is missing!");
            }
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, streamInput.getFormat().getSampleRate(), 16, streamInput.getFormat().getChannels(), streamInput.getFormat().getChannels() * 2, streamInput.getFormat().getSampleRate(), false);
            stream = new AmplitudeAudioInputStream(AudioSystem.getAudioInputStream(decodedFormat, streamInput));
        }
        return stream;
    }
