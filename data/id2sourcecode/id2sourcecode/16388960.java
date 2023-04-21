    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        ChannelPresence presence = channelService.parsePresence(req);
        if (presence.isConnected()) {
            connectedClientIds.add(presence.clientId());
        } else {
            connectedClientIds.remove(presence.clientId());
        }
    }
