    private Level getLevel(final Attribute attribute, final int index) {
        Level level = null;
        FixtureChannel channel = attribute.getChannel(index);
        int channelIndex = channel.getNumber() - 1;
        if (channelIndex >= 0) {
            Buffer buffer = context.getLanbox().getMixer();
            level = buffer.getLevels().get(channelIndex);
        }
        return level;
    }
