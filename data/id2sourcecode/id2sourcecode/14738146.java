    @Override
    protected void onConnect() {
        if (!isLive) {
            disconnect();
            return;
        }
        joinChannel(bot.getChannel());
    }
