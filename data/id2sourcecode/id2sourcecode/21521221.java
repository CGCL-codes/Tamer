    public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat[] formats = getTargetFormats(targetFormat.getEncoding(), sourceFormat, false);
        if (formats != null && formats.length > 0) {
            if (sourceFormat.equals(targetFormat)) {
                return sourceStream;
            } else if (sourceFormat.getChannels() == targetFormat.getChannels() && sourceFormat.getSampleSizeInBits() == targetFormat.getSampleSizeInBits() && !targetFormat.isBigEndian() && sourceFormat.getEncoding().equals(FlacEncoding.FLAC) && targetFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
                return new Flac2PcmAudioInputStream(sourceStream, targetFormat);
            } else if (sourceFormat.getChannels() == targetFormat.getChannels() && sourceFormat.getSampleSizeInBits() == targetFormat.getSampleSizeInBits() && sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) && targetFormat.getEncoding().equals(FlacEncoding.FLAC)) {
                throw new IllegalArgumentException("FLAC encoder not yet implemented");
            } else {
                throw new IllegalArgumentException("unable to convert " + sourceFormat.toString() + " to " + targetFormat.toString());
            }
        } else {
            throw new IllegalArgumentException("conversion not supported");
        }
    }
