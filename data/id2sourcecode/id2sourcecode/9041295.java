    @Override
    public void run() throws Exception {
        try {
            Channel chan = AppContext.getChannelManager().getChannel(channelName);
            if (!chan.hasSessions()) AppContext.getDataManager().removeObject(chan);
        } catch (NameNotBoundException e) {
        }
    }
