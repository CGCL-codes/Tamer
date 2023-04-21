    public void update() {
        removeAllChildren();
        ChatOptions opt = ChatApp.getChatApp().getOptions();
        for (int j = 0; j < opt.getFavoriteChannels().getChannelCount(); j++) {
            add(new ChannelNode(opt.getFavoriteChannels().getChannel(j)));
        }
    }
