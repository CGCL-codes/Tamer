    public List<TunerChannel> getChannelList() {
        return objectList(GSTTUNER_API.gst_tuner_list_channels(this), new ListElementCreator<TunerChannel>() {

            public TunerChannel create(Pointer pointer) {
                return channelFor(pointer, true);
            }
        });
    }
