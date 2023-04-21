    public static AudioInputStream createDecodedAudioInputStream(AudioInputStream audioInputStream) {
        AudioFormat sourceFormat = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, sourceFormat, LimeWirePlayer.EXTERNAL_BUFFER_SIZE);
        if (AudioSystem.isLineSupported(info)) {
            return audioInputStream;
        } else {
            int nSampleSizeInBits = sourceFormat.getSampleSizeInBits();
            if (nSampleSizeInBits <= 0) nSampleSizeInBits = 16;
            if ((sourceFormat.getEncoding() == AudioFormat.Encoding.ULAW) || (sourceFormat.getEncoding() == AudioFormat.Encoding.ALAW)) nSampleSizeInBits = 16;
            if (nSampleSizeInBits != 8) nSampleSizeInBits = 16;
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), false);
            info = new DataLine.Info(SourceDataLine.class, targetFormat, LimeWirePlayer.EXTERNAL_BUFFER_SIZE);
            return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
        }
    }
