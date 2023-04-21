    private void reloadChannels() {
        reloadChannels.setEnabled(false);
        final ChannelListRequest channelListRequest = new ChannelListRequest(view);
        VFSManager.runInWorkThread(channelListRequest);
        VFSManager.runInAWTThread(new Runnable() {

            public void run() {
                setChannelList(channelListRequest.getChannelList());
                reloadChannels.setEnabled(true);
            }
        });
    }
