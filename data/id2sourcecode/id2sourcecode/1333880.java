    private Clip initializeClip(final AudioCue cue) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if ((clips[cue.ordinal()] != null) && (clips[cue.ordinal()].isOpen())) {
            return clips[cue.ordinal()];
        }
        InputStream sampleStream = this.getClass().getResourceAsStream(cue.getResourcePath());
        AudioInputStream stream = AudioSystem.getAudioInputStream(sampleStream);
        AudioFormat format = stream.getFormat();
        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
            stream = AudioSystem.getAudioInputStream(format, stream);
        }
        DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), (int) stream.getFrameLength() * format.getFrameSize());
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.addLineListener(new LineListener() {

            public void update(LineEvent evt) {
                if (evt.getType() != LineEvent.Type.STOP) return;
                clips[cue.ordinal()].stop();
                clips[cue.ordinal()].setFramePosition(0);
            }
        });
        clip.open(stream);
        return clip;
    }
