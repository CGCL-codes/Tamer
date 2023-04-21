    public void play(File soundfile, Mixer mixer) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(soundfile);
            AudioFormat input = stream.getFormat();
            AudioFormat output = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, input.getSampleRate(), 16, input.getChannels(), input.getChannels() * 2, input.getSampleRate(), false);
            AudioInputStream outstream = AudioSystem.getAudioInputStream(output, stream);
            new RunSoundPlayer((SourceDataLine) mixer.getLine(mixer.getSourceLineInfo()[0]), outstream).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
