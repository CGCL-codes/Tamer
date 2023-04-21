    protected boolean doIsBound(String name) {
        DatagramChannel dc = null;
        Listener listener = container.getListener(name);
        dc = (DatagramChannel) listener.getChannel();
        if (dc != null) {
            return dc.socket().isBound();
        } else {
            return false;
        }
    }
