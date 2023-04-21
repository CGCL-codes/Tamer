    public static ReadableByteChannel getChannel(InputStream inputStream) {
        return (inputStream != null) ? Channels.newChannel(inputStream) : null;
    }
