    @Override
    protected AbstractSelectableChannel getChannel() throws IOException {
        DatagramChannel c;
        if (isWaitResponse()) {
            c = DatagramChannelWithTimeouts.open();
            ((DatagramChannelWithTimeouts) c).setReadTimeout(getTimeoutAsInt());
        } else {
            c = DatagramChannel.open();
        }
        int port = Integer.parseInt(getPort());
        c.connect(new InetSocketAddress(getHostName(), port));
        return c;
    }
