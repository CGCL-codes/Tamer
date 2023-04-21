    public void playSound(InputStream is) {
        if (is == null) {
            return;
        }
        isRunning = true;
        AudioInputStream ain = null;
        try {
            ain = AudioSystem.getAudioInputStream(is);
            if (ain == null) {
                return;
            }
            AudioFormat format = ain.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat temp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                ain = AudioSystem.getAudioInputStream(temp, ain);
                format = temp;
            }
            rawplay(format, ain, volume);
        } catch (Exception e) {
        } finally {
            if (ain != null) {
                try {
                    ain.close();
                } catch (Exception e) {
                }
            }
        }
    }
