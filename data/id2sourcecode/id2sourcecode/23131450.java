    public int getChannels() {
        if (info.channels == 1) return AL10.AL_FORMAT_MONO16;
        return AL10.AL_FORMAT_STEREO16;
    }
