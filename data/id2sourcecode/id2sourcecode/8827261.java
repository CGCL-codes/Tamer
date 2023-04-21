    public AudioInputStreamSource(AudioInputStream aisin) {
        AudioFormat aisf = aisin.getFormat();
        ais = (aisf.getEncoding() == AudioFormat.Encoding.PCM_SIGNED && aisf.getSampleSizeInBits() == 16) ? aisin : AudioSystem.getAudioInputStream(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, aisf.getSampleRate(), 16, aisf.getChannels(), 2 * aisf.getFrameSize() * aisf.getChannels(), aisf.getFrameRate(), aisf.isBigEndian()), aisin);
    }
