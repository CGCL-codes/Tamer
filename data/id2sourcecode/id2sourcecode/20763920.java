    static AudioInputStream getAudioInputStream(File file) throws UnsupportedAudioFileException, IOException {
        if (!JSoundSystem.soundIsSupported(file)) throw new UnsupportedAudioFileException("Audio file not supported: " + file.getAbsolutePath());
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        AudioInputStream rawstream = AudioSystem.getAudioInputStream(in);
        AudioFormat decodedFormat = rawstream.getFormat();
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".ogg")) {
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, decodedFormat.getSampleRate(), 16, 2, decodedFormat.getChannels() * 2, decodedFormat.getSampleRate(), false);
        } else if (fileName.endsWith(".mp3")) {
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, decodedFormat.getSampleRate(), 16, decodedFormat.getChannels(), decodedFormat.getChannels() * 2, decodedFormat.getSampleRate(), false);
        }
        return AudioSystem.getAudioInputStream(decodedFormat, rawstream);
    }
