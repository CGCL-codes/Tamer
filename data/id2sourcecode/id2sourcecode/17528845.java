    public void removeClientData(String account) {
        ChannelManager man = AppContext.getChannelManager();
        DataManager dman = AppContext.getDataManager();
        SGSClientData data = getClientData(account);
        String channelName = data.get(SGSClientData.CURRENT_CHANNEL_NAME, String.class);
        if (!Str.nostr(channelName)) {
            Channel ch = man.getChannel(channelName);
            SGSBusinessProcess.onLeaveChannelNotify(this, data.get(SGSClientData.NSERVER_ID, Integer.class), data.get(SGSClientData.NSERVER_CHANNEL_ID, Integer.class), data.get(SGSClientData.CLIENT_SESSION, ClientSession.class), ch);
        }
        SGSClientData toRemove = null;
        try {
            toRemove = dman.getBinding(SGSClientData.getBinding(account), SGSClientData.class);
            dman.removeBinding(SGSClientData.getBinding(account));
            dman.removeObject(toRemove);
        } catch (NameNotBoundException ex) {
        }
        SGSNSessionListener nl = SGSNSessionListener.getInstance();
        if (nl != null) {
            DestroyClient dc = new DestroyClient();
            dc.account = account;
            try {
                nl.send(Converter.getInstance().convert(dc));
            } catch (Exception ex) {
                logger.warn("unable to notify main server of client removal", ex);
            }
        }
    }
