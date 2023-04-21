    public static ChannelManager getChannelManager() {
        try {
            return InternalContext.getManagerLocator().getChannelManager();
        } catch (IllegalStateException ise) {
            throw new ManagerNotFoundException("ManagerLocator is " + "unavailable", ise);
        }
    }
