    private static AudioInputStream convertToPCM(AudioInputStream audioInputStream) {
        AudioFormat format = audioInputStream.getFormat();
        if ((format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) && (format.getEncoding() != AudioFormat.Encoding.PCM_UNSIGNED)) {
            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), format.isBigEndian());
            audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
        }
        return audioInputStream;
    }
