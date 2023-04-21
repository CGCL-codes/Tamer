    public static WritableByteChannel getChannel(OutputStream outputStream) throws IOException {
        return (outputStream != null) ? Channels.newChannel(outputStream) : null;
    }
