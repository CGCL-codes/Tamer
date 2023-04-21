    private void playSoundFile() {
        String extension = TFileUtils.getExtension(soundFilePath);
        if (extension.equals("mp3")) {
            prueba = new TMp3Sound(soundFilePath);
            prueba.TPlay();
            stopSoundButton.setEnabled(true);
            playSoundButton.setEnabled(false);
            clearSoundButton.setEnabled(false);
            selectSoundButton.setEnabled(false);
        } else {
            try {
                int sampleSizeInBits = 16;
                int internalBufferSize = AudioSystem.NOT_SPECIFIED;
                File soundFile = new File(soundFilePath);
                audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                audioFormat = audioInputStream.getFormat();
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, internalBufferSize);
                boolean isSuportedDirectly = AudioSystem.isLineSupported(dataLineInfo);
                if (!isSuportedDirectly) {
                    AudioFormat sourceFormat = audioFormat;
                    AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), sampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (sampleSizeInBits / 8), sourceFormat.getSampleRate(), false);
                    audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                    audioFormat = audioInputStream.getFormat();
                }
                dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat, internalBufferSize);
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open(audioFormat, internalBufferSize);
                stopPlayback = false;
                stopSoundButton.setEnabled(true);
                playSoundButton.setEnabled(false);
                clearSoundButton.setEnabled(false);
                selectSoundButton.setEnabled(false);
                audioPlayThread = new AudioPlayThread();
                audioPlayThread.start();
            } catch (UnsupportedAudioFileException e) {
                JOptionPane.showMessageDialog(null, TLanguage.getString("TSoundChooser.INVALID_FORMAT_ERROR"), TLanguage.getString("ERROR") + "!", JOptionPane.ERROR_MESSAGE);
            } catch (LineUnavailableException e) {
                JOptionPane.showMessageDialog(null, TLanguage.getString("TSoundChooser.PLAY_FAILURE_ERROR"), TLanguage.getString("ERROR") + "!", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, TLanguage.getString("TSoundChooser.OPEN_FILE_ERROR"), TLanguage.getString("ERROR") + "!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
