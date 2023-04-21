    protected InputStream getInputStream() throws IOException {
        if (null == _in) {
            final SocketChannel channel = getChannel();
            if (null == channel) throw new SocketException("No current channel to retrieve " + InputStream.class.getName() + " instance from");
            final Socket s = channel.socket();
            if (null == s) throw new SocketException("No current socket to retrieve " + InputStream.class.getName() + " instance from");
            if (null == (_in = s.getInputStream())) throw new StreamCorruptedException("No input stream from socket");
        }
        return _in;
    }
