    public Channel getChannel() {
        if (getDestination() != null && getContext().isPublic()) {
            return getServer().getChannel(getDestination());
        }
        return null;
    }
