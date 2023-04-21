    protected void onCurrentChannel(GetCurrentChannelResponse response) {
        this.client.getState().setCurrentChannel(response.getChannel());
    }
