    public ByteChannel getChannel() throws IOException {
        if (channel == null) {
            channel = new StreamingByteChannel(socket.getInputStream(), socket.getOutputStream());
        }
        return channel;
    }
