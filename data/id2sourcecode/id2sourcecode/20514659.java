    private boolean writeRemoteSocket(Buffer buffer, SocksSocket client) {
        if (null != client) {
            try {
                client.getOutputStream().write(buffer.getRawBuffer(), buffer.getReadIndex(), buffer.readableBytes());
            } catch (IOException e) {
                logger.error("Failed to write socks proxy client.");
                return false;
            }
        }
        return true;
    }
