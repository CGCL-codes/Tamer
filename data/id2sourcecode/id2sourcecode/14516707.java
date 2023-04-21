    private void playSound() {
        Clip clip = cache.get(fileName);
        try {
            if (clip == null) {
                AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(fileName));
                AudioFormat format = stream.getFormat();
                if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                    format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                    stream = AudioSystem.getAudioInputStream(format, stream);
                }
                DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), (int) stream.getFrameLength() * format.getFrameSize());
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(stream);
                cache.put(fileName, clip);
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (IOException e) {
        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e) {
        }
    }
