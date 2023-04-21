    public String getChannelNum() throws Exception {
        return SageApi.StringApi("GetChannelNumber", new Object[] { sageAiring });
    }
