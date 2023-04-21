    public void close(MsgContext ep) throws IOException {
        Socket s = (Socket) ep.getNote(socketNote);
        SelectionKey key = s.getChannel().keyFor(selector);
        if (key != null) {
            key.cancel();
        }
        s.close();
    }
