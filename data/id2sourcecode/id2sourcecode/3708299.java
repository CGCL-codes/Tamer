    public boolean matches(EpgEvent evt) {
        if (evt == null) return false;
        if (channels.isEmpty()) return true;
        String channel = evt.getChannel().getName();
        return channels.contains(channel);
    }
