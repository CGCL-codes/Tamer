    @Override
    public void appendCommand(final StringBuilder b) {
        String name = Util.nameOf(getClass());
        b.append(name);
        b.append("(bufferId=");
        b.append(bufferId);
        b.append("\n  ");
        for (ChannelAttribute indicator : attributes) {
            b.append(indicator.getChannelId());
            b.append("[");
            b.append(indicator.isEnabled());
            b.append("] ");
        }
    }
