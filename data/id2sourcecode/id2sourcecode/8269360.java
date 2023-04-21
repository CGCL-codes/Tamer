    public TunerChannel getChannelByName(String name) {
        Pointer ptr = GSTTUNER_API.gst_tuner_find_channel_by_name(this, name);
        if (ptr == null) {
            return null;
        }
        return new TunerChannel(this, ptr, false, true);
    }
