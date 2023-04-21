    public void onGetChannelListResponseRadio(GetChannelListResponse response) {
        this.state.setStations(ServiceType.RADIO, response.getChannels());
    }
