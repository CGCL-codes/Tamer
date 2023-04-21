    public static void shutdownChannel(NamedChannel namedChannel) {
        if (namedChannel == null) {
            return;
        }
        SocketChannel channel = namedChannel.getChannel();
        if (channel == null) {
            return;
        }
        try {
            channel.socket().shutdownInput();
        } catch (IOException ignore) {
        }
        try {
            channel.socket().shutdownOutput();
        } catch (IOException e) {
        }
        try {
            channel.close();
        } catch (IOException e) {
        }
    }
