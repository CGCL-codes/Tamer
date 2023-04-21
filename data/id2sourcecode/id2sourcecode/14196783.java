    public static void playMP3Sound(String fileName, String cpBaseDataPath) {
        File file = new File(fileName);
        if (file.isAbsolute() == false) {
            file = null;
            file = new File(cpBaseDataPath + fileName);
        }
        AudioInputStream in = null;
        try {
            in = AudioSystem.getAudioInputStream(file);
        } catch (javax.sound.sampled.UnsupportedAudioFileException ex) {
            System.err.println(ex.getMessage());
            return;
        } catch (java.io.IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        try {
            playMP3raw(decodedFormat, din);
            in.close();
        } catch (java.io.IOException ex) {
            System.err.println("Error playing file: " + ex.getMessage());
        }
    }
