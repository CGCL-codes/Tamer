    private boolean checkConnection(String name) {
        Channel tempChannel = ChannelFactory.defaultFactory().getChannel(name);
        try {
            tempChannel.checkConnection();
        } catch (ConnectionException e) {
            return false;
        }
        return true;
    }
