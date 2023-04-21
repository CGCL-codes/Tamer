    public AudioFormat getCurrentAudioFormat(URL url) {
        AudioFormat decodedFormat = null;
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(url);
            AudioFormat baseFormat = in.getFormat();
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return decodedFormat;
    }
