    public boolean isConnected() {
        if (bus == null || bus.getChannel() == null) {
            return false;
        }
        return bus.getChannel().isConnected();
    }
