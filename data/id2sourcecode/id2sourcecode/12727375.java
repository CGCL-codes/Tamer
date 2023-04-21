    public void getAllChannels() {
        for (String lineup : sageApi.global.GetAllLineups()) getChannelsOnLineup(lineup);
    }
