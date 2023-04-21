    private void init() {
        ChannelTagAdapter channelAdapter = new ChannelTagAdapter(this);
        this.setTagFormatter(channelAdapter.getChannelTagFormatter());
        if (!Utility.isEmpty(this.getEnabledChannelStr())) {
            String channelStr = this.getEnabledChannelStr();
            String[] channels = channelStr.split("\\|");
            for (int i = 0; i < channels.length; i++) {
                if (channels[i].indexOf(this.getFilterName()) >= 0) {
                    String[] channelStyle = channels[i].split("_");
                    this.setPageSize(Integer.parseInt(channelStyle[2]));
                }
            }
        }
    }
