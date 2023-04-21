    public static ReadableByteChannel getChannel(InputStream inputStream) throws IOException {
        return (inputStream != null) ? Channels.newChannel(inputStream) : null;
    }
