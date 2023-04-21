    public List<ColorBalanceChannel> getChannelList() {
        return objectList(GSTCOLORBALANCE_API.gst_color_balance_list_channels(this), new ListElementCreator<ColorBalanceChannel>() {

            public ColorBalanceChannel create(Pointer pointer) {
                return channelFor(pointer, true);
            }
        });
    }
