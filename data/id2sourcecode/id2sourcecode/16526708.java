    public void playSound(InputStream is) {
        if (is == null) {
            return;
        }
        this.isRunning = true;
        AudioInputStream din = null;
        AudioInputStream in = null;
        try {
            in = AudioSystem.getAudioInputStream(is);
            if (in == null) {
                return;
            }
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            rawplay(decodedFormat, din, volume);
        } catch (Exception e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
    }
