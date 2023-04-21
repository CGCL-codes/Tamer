    @Override
    public String reconnect_channel(String id) {
        log.severe("Ok lets reconnect  " + id);
        token = ChannelServiceFactory.getChannelService().createChannel(id);
        if (token != null) {
            return token;
        } else {
            log.severe("token null can't create");
            return null;
        }
    }
