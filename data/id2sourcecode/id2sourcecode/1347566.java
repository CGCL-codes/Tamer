    public String getChannel() throws Exception {
        String channel = SageApi.StringApi("GetFavoriteChannel", new Object[] { sageFavorite });
        return (channel != null) ? channel : "";
    }
