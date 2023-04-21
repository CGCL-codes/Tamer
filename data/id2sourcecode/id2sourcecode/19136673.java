    public void loadFromFile(File soundFile) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            audioFormat = audioInputStream.getFormat();
            System.out.println(getAudioFormat());
            if ((audioFormat.getChannels() != 2) || (audioFormat.getSampleRate() != 44100) || (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED)) {
                System.out.println("Unable to load! Currently only 44kHz SIGNED 16bit Stereo is supported.");
                return;
            }
            loadRawFromStream(audioInputStream);
        } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBWave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
