    @Override
    public AudioFormat[] getTargetFormats(final Encoding targetEncoding, final AudioFormat sourceFormat) {
        if (Encoding.ULAW.equals(sourceFormat.getEncoding())) {
            return new AudioFormat[] { new AudioFormat(Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), 2 * sourceFormat.getChannels(), sourceFormat.getFrameRate(), false) };
        }
        return new AudioFormat[] {};
    }
