    protected Clip loadSound(InputStream soundStream) {
        AudioInputStream stream = null;
        Clip clip = null;
        if (soundStream == null) return null;
        try {
            stream = AudioSystem.getAudioInputStream(new BufferedInputStream(soundStream, 2048));
        } catch (Exception ex) {
            Debug.signal(Debug.ERROR, this, "Failed to load sound : " + ex);
            return null;
        }
        try {
            AudioFormat format = stream.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmp = new javax.sound.sampled.AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(tmp, stream);
                format = tmp;
            }
            DataLine.Info info = new DataLine.Info(javax.sound.sampled.Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
        } catch (Exception ex) {
            Debug.signal(Debug.ERROR, this, "Failed to read sound : " + ex);
            return null;
        }
        return clip;
    }
