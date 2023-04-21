    @Nonnull
    public static ReadableByteChannel asReadableByteChannel(final InputStream delegate) {
        if (delegate instanceof ReadableByteChannel) {
            return (ReadableByteChannel) delegate;
        } else if (delegate instanceof FileInputStream) {
            final ReadableByteChannel ch = ((FileInputStream) delegate).getChannel();
            if (ch != null) return ch;
        }
        return new IOChannels.InputStreamChannel(delegate);
    }
