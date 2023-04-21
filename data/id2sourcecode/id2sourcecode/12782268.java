    public AudioInputStream getAudioInputStream(AudioFormat.Encoding targetEncoding, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat.Encoding sourceEncoding = sourceFormat.getEncoding();
        if (sourceEncoding.equals(targetEncoding)) {
            return sourceStream;
        } else {
            AudioFormat targetFormat = null;
            if (!isConversionSupported(targetEncoding, sourceStream.getFormat())) {
                throw new IllegalArgumentException("Unsupported conversion: " + sourceStream.getFormat().toString() + " to " + targetEncoding.toString());
            }
            if (sourceEncoding.equals(AudioFormat.Encoding.ALAW) && targetEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
                targetFormat = new AudioFormat(targetEncoding, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), 2 * sourceFormat.getChannels(), sourceFormat.getSampleRate(), sourceFormat.isBigEndian());
            } else if (sourceEncoding.equals(AudioFormat.Encoding.PCM_SIGNED) && targetEncoding.equals(AudioFormat.Encoding.ALAW)) {
                targetFormat = new AudioFormat(targetEncoding, sourceFormat.getSampleRate(), 8, sourceFormat.getChannels(), sourceFormat.getChannels(), sourceFormat.getSampleRate(), false);
            } else {
                throw new IllegalArgumentException("Unsupported conversion: " + sourceStream.getFormat().toString() + " to " + targetEncoding.toString());
            }
            return getAudioInputStream(targetFormat, sourceStream);
        }
    }
