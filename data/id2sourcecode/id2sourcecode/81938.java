    public void play() {
        File soundFile = new File(filename);
        if (!soundFile.exists()) {
            System.err.println("OGG file not found: " + filename);
            return;
        }
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        AudioInputStream decodedInputStream = null;
        try {
            if (audioInputStream != null) {
                AudioFormat base_format = audioInputStream.getFormat();
                AudioFormat decoded_format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, base_format.getSampleRate(), 16, base_format.getChannels(), base_format.getChannels() * 2, base_format.getSampleRate(), false);
                decodedInputStream = AudioSystem.getAudioInputStream(decoded_format, audioInputStream);
                rawplay(decoded_format, decodedInputStream);
                audioInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
