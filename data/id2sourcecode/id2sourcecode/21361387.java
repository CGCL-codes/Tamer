    private boolean loadSound(File file) {
        newSoundDuration = 0;
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(tmp, stream);
                format = tmp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
            newSoundClip = (Clip) AudioSystem.getLine(info);
            newSoundClip.open(stream);
            newSoundFrameLen = (int) stream.getFrameLength();
            newSoundDuration = (int) (newSoundClip.getBufferSize() / (newSoundClip.getFormat().getFrameSize() * newSoundClip.getFormat().getFrameRate()));
            return true;
        } catch (Exception ex) {
            newSoundClip = null;
            return false;
        }
    }
