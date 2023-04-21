    protected void requestBookConnection() {
        try {
            final Channel bookChannel = getNode().getChannel(MagnetMainSupply.FIELD_BOOK_HANDLE);
            bookChannel.requestConnection();
        } catch (NoSuchChannelException exception) {
        }
    }
