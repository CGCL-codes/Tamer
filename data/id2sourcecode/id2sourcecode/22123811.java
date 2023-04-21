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
            if (AudioFormat.Encoding.ULAW.equals(sourceEncoding) && AudioFormat.Encoding.PCM_SIGNED.equals(targetEncoding)) {
                targetFormat = new AudioFormat(targetEncoding, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), 2 * sourceFormat.getChannels(), sourceFormat.getSampleRate(), sourceFormat.isBigEndian());
            } else if (AudioFormat.Encoding.PCM_SIGNED.equals(sourceEncoding) && AudioFormat.Encoding.ULAW.equals(targetEncoding)) {
                targetFormat = new AudioFormat(targetEncoding, sourceFormat.getSampleRate(), 8, sourceFormat.getChannels(), sourceFormat.getChannels(), sourceFormat.getSampleRate(), false);
            } else {
                throw new IllegalArgumentException("Unsupported conversion: " + sourceStream.getFormat().toString() + " to " + targetEncoding.toString());
            }
            return getAudioInputStream(targetFormat, sourceStream);
        }
    }
