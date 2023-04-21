    private Level getChannelLevel(final int channelIndex) {
        Buffer buffer = getContext().getLanbox().getMixer();
        return buffer.getLevels().get(channelIndex);
    }
