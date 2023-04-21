    @Override
    public AudioFormat[] getTargetFormats(Encoding targetEncoding, AudioFormat fmt) {
        if (targetEncoding == Encoding.PCM_SIGNED) {
            if (!fmt.getEncoding().equals(OggCodec.Vorbis.encoding)) {
                return new AudioFormat[0];
            }
            return new AudioFormat[] { new AudioFormat(fmt.getSampleRate(), fmt.getSampleSizeInBits(), fmt.getChannels(), true, fmt.isBigEndian()) };
        }
        return new AudioFormat[0];
    }
