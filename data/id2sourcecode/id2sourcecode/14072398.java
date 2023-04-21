    private void registerNewChannel() {
        try {
            ChannelInterestOps channelInterestOps = null;
            while ((channelInterestOps = registerChannelQueue.poll()) != null) {
                SelectableChannel channel = channelInterestOps.getChannel();
                int interestOps = channelInterestOps.getInterestOps();
                Connector connector = channelInterestOps.getConnector();
                channel.configureBlocking(false);
                channel.register(selector, interestOps, connector);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
