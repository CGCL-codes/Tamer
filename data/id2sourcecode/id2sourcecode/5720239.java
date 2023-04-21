    public int getALFormat() throws E3DInvalidSoundFormatException {
        if (audioStream == null || audioStream.getFormat() == null) return -1;
        int format = -1;
        if (audioStream.getFormat().getChannels() == 1) {
            if (audioStream.getFormat().getSampleSizeInBits() == 8) format = AL10.AL_FORMAT_MONO8; else if (audioStream.getFormat().getSampleSizeInBits() == 16) format = AL10.AL_FORMAT_MONO16;
        } else if (audioStream.getFormat().getChannels() == 2) {
            if (audioStream.getFormat().getSampleSizeInBits() == 8) format = AL10.AL_FORMAT_STEREO8; else if (audioStream.getFormat().getSampleSizeInBits() == 16) format = AL10.AL_FORMAT_STEREO16;
        }
        if (format == -1) throw new E3DInvalidSoundFormatException("Channels: " + audioStream.getFormat().getChannels() + "and SampleSize: " + audioStream.getFormat().getSampleSizeInBits() + " bits " + "is not a support wav format.  Only mono and stereo of 8 and 16 bit sample size are supported.");
        return format;
    }
