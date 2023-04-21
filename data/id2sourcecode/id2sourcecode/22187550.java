    @Override
    public int getNumberOfTracePositions() {
        return getChannelGroup().getAChannel().getPositions().array().length;
    }
