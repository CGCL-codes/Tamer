    public void onGetChannelListResponseTV(GetChannelListResponse response) {
        this.state.setStations(ServiceType.TV, response.getChannels());
    }
