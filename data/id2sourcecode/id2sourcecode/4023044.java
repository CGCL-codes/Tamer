    public boolean startFilePlayback(File file) {
        audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (Exception e) {
            log.warning("Exception: Sound: getAudioInputStream: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        AudioFormat audioFormat = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        if (!AudioSystem.isLineSupported(info)) {
            AudioFormat sourceFormat = audioFormat;
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
            audioFormat = audioInputStream.getFormat();
            info = new DataLine.Info(SourceDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(info)) {
                log.warning("Error Sound: DataLine not supported");
                return false;
            }
        }
        if (line != null) {
            line.close();
            line = null;
        }
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.addLineListener(new LineListener() {

                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        synchronized (this) {
                            mode = MODE_READY;
                        }
                    }
                }
            });
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            log.warning("WARNING: Sound: Couldn't get sound line.\n" + "Sound device may be in use by another application.");
            return false;
        } catch (Exception e) {
            log.warning("Exception: Sound: getLine: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        line.start();
        written = 0;
        read = 0;
        return true;
    }
