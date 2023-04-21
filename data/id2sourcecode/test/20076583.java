    public void disconnected(boolean reason) {
        System.out.println(name + " disconnected");
        PartEvent event = new PartEvent(name, "world");
        AppContext.getChannelManager().getChannel(CastaneaServer.WORLD).send(null, MessageUtil.encode(event));
    }
