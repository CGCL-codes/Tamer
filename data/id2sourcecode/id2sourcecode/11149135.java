    public int getChannelCount(final String groupType) {
        return _model.getLoggerSession(groupType).getChannelGroup().getChannelCount();
    }
