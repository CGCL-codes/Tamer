    public final String getChannelId() {
        String rv = null;
        if (event != null && event.getChannel() != null) rv = event.getChannel().getShortId();
        return rv;
    }
