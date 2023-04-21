    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        ChannelPresence presence = channelService.parsePresence(req);
        id = presence.clientId();
        injector = Guice.createInjector(new GuiceModule());
        channel_connectedManager = injector.getInstance(Channel_connectedManager.class);
        if (!channel_connectedManager.clientConnected(id)) {
            log.warning("client " + id + " not registered as connected ??!!");
        }
    }
