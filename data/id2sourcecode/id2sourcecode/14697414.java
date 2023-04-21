    @Override
    public void onMessage(Message message) {
        parseMessage(message);
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        for (PageEntity page : getDao().getPageDao().select()) {
            if (filter(page)) {
                channelService.sendMessage(new ChannelMessage(clientId, createHitJSON(page)));
            }
        }
        channelService.sendMessage(new ChannelMessage(clientId, "({end:true})"));
    }
