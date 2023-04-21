    protected PsdChannelInfo getChannelInfoById(int id) {
        for (PsdChannelInfo info : channelsInfo) {
            if (info.getId() == id) {
                return info;
            }
        }
        throw new RuntimeException("channel info for id " + id + " not found.");
    }
