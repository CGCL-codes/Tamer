    public final SocketChannel getChannel() {
        if (self == this) {
            return super.getChannel();
        } else {
            return self.getChannel();
        }
    }
