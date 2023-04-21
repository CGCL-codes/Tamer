    public void setConfiguration(Configuration cfg) throws ConfigurationException {
        timeout = cfg.getInt("timeout");
        bounce = cfg.getBoolean("bounce");
        String muxName = cfg.get("destination-mux", null);
        String channelName = cfg.get("destination-channel", null);
        try {
            if (muxName != null) destMux = ISOMUX.getMUX(muxName); else if (channelName != null) destChannel = BaseChannel.getChannel(channelName);
        } catch (NotFoundException e) {
            throw new ConfigurationException(e);
        }
    }
