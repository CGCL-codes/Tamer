    public WritableByteChannel getChannel() {
        if (socket != null) return socket;
        if (is instanceof FileOutputStream) return ((FileOutputStream) is).getChannel();
        return null;
    }
