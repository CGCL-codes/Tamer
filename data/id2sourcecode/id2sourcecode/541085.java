    public void testPlay(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File(filename);
        AudioInputStream in = AudioSystem.getAudioInputStream(file);
        AudioInputStream din = null;
        if (in != null) {
            AudioFormat baseFormat = in.getFormat();
            System.out.println(in.getFormat());
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            rawplay(decodedFormat, din);
            in.close();
        }
    }
