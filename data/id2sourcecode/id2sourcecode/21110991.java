    public AudioInputStream getAudioInputStream(AudioFormat.Encoding targetEncoding, AudioInputStream sourceStream) {
        if (isConversionSupported(targetEncoding, sourceStream.getFormat())) {
            AudioFormat sourceFormat = sourceStream.getFormat();
            AudioFormat targetFormat = new AudioFormat(targetEncoding, sourceFormat.getSampleRate(), sourceFormat.getSampleSizeInBits(), sourceFormat.getChannels(), sourceFormat.getFrameSize(), sourceFormat.getFrameRate(), sourceFormat.isBigEndian());
            return getAudioInputStream(targetFormat, sourceStream);
        } else {
            throw new IllegalArgumentException("Unsupported conversion: " + sourceStream.getFormat().toString() + " to " + targetEncoding.toString());
        }
    }
