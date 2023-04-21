    public int getChannels() {
        if (channels() == 1) {
            if (depth() == 8) {
                return AL10.AL_FORMAT_MONO8;
            } else if (depth() == 16) {
                return AL10.AL_FORMAT_MONO16;
            } else {
                throw new JmeException("Illegal sample size");
            }
        } else if (channels() == 2) {
            if (depth() == 8) {
                return AL10.AL_FORMAT_STEREO8;
            } else if (depth() == 16) {
                return AL10.AL_FORMAT_STEREO16;
            } else {
                throw new JmeException("Illegal sample size");
            }
        } else {
            throw new JmeException("Only mono or stereo is supported");
        }
    }
