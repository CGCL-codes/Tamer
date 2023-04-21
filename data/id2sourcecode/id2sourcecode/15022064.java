    public void onGetCurrentChannelResponse(GetCurrentChannelResponse response) {
        this.state.setCurrentChannel(response.getChannel());
    }
