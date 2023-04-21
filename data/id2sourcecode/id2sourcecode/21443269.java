    public Channel getChannelByNumber(Recorder r, int channelNumber) throws SQLException {
        return getDbCon().getChannel(getDbCon().getChannelIDBYChannum(getDbCon().getSourceByRecorder(r.getId()), channelNumber));
    }
