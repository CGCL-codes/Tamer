    public synchronized Object getChannelManager() {
        try {
            this.fGetChannelManager.setAccessible(true);
            return this.fGetChannelManager.invoke(this.fAppContext);
        } catch (final Exception e) {
            return null;
        } finally {
            this.fGetChannelManager.setAccessible(false);
        }
    }
