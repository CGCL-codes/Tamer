    public void play(File soundfile, Mixer mixer) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(soundfile);
            AudioFormat input = stream.getFormat();
            AudioFormat output = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, input.getSampleRate(), 16, input.getChannels(), input.getChannels() * 2, input.getSampleRate(), false);
            AudioInputStream outstream = AudioSystem.getAudioInputStream(output, stream);
            Clip clip = (Clip) mixer.getLine(mixer.getSourceLineInfo()[1]);
            clip.open(outstream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            new RunMusicPlayer(clip).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
