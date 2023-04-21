    public void loadSound(String filename, String sound) {
        synchronized (soundClipMap) {
            AudioInputStream ais = null;
            try {
                ais = AudioSystem.getAudioInputStream(new File(filename));
            } catch (UnsupportedAudioFileException ex) {
                ex.printStackTrace();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
            AudioFormat audioFormat = ais.getFormat();
            if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
                ais = AudioSystem.getAudioInputStream(targetFormat, ais);
                audioFormat = ais.getFormat();
            }
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            Clip clip = null;
            try {
                clip = (Clip) AudioSystem.getLine(info);
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
                return;
            }
            try {
                clip.open(ais);
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }
            soundClipMap.put(sound, clip);
            FloatControl c = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            c.setValue(calcGain(c, masterGain));
        }
    }
