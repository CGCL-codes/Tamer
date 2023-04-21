    @Override
    protected void setUp(soundTheme theme, soundEffects effect, int volume) {
        try {
            URL url = this.getClass().getResource("tracks/" + theme + "_" + effect + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(volume);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("It was not possible to start the sound");
        } catch (Exception e) {
            System.err.println("It was not possible to start the sound");
        }
    }
