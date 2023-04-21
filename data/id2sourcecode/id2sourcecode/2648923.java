    private AudioInputStream convertStream(AudioInputStream stream) {
        AudioFormat format = stream.getFormat();
        if (format.getEncoding().equals(Encoding.PCM_SIGNED)) if (format.getSampleSizeInBits() == 16) if (!format.isBigEndian()) {
            return stream;
        }
        AudioFormat convformat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), 2 * format.getChannels(), format.getSampleRate(), false);
        if (!AudioSystem.isConversionSupported(convformat, format)) return null;
        return AudioSystem.getAudioInputStream(convformat, stream);
    }
