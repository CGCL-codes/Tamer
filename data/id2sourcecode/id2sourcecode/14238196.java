    public static boolean playSound(String path) {
        if (path != null && !"".equals(path.trim())) {
            File location = new File(path);
            if (location.exists() && checkSoundFile(path)) {
                try {
                    AudioInputStream stream = AudioSystem.getAudioInputStream(location);
                    AudioFormat format = stream.getFormat();
                    if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                        AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                        stream = AudioSystem.getAudioInputStream(tmp, stream);
                        format = tmp;
                    }
                    DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
                    Line line = AudioSystem.getLine(info);
                    Clip clip = (Clip) line;
                    clip.open(stream);
                    clip.start();
                } catch (Exception e) {
                    log.log(Level.SEVERE, "", e);
                    return false;
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
                return true;
            }
        }
        return false;
    }
