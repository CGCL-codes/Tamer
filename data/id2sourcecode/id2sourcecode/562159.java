    public void onChannelJoin(ServerEvent event) {
        Channel chan = (Channel) event.getChannel();
        System.out.println("Joined " + chan);
        chan.addChannelListener(new ChannelLogger());
    }
