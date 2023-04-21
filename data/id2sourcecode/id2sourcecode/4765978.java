    @Override
    protected Object internalRun() {
        try {
            ChannelBuilder cb = new ChannelBuilder(link);
            Channel channel = cb.getChannel();
            status = "[Spider] Successfully load " + link;
            return channel;
        } catch (ChannelBuilderException e) {
            e.printStackTrace();
            status = "[Spider] Fail to load " + link;
        }
        return null;
    }
