    public int[] getVolume() {
        int[] volume = new int[getChannelCount()];
        GSTMIXER_API.gst_mixer_get_volume(mixer, this, volume);
        return volume;
    }
