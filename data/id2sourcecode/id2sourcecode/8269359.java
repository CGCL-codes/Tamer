    public TunerChannel getChannel() {
        Pointer ptr = GSTTUNER_API.gst_tuner_get_channel(this);
        if (ptr == null) {
            return null;
        }
        return new TunerChannel(this, ptr, false, true);
    }
