    protected boolean isOpen() {
        Channel channel = this.notificationBus.getChannel();
        boolean open = channel.isOpen();
        return open;
    }
