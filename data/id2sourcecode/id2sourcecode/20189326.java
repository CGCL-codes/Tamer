    @Nonnull
    public static WritableByteChannel asWritableByteChannel(final OutputStream delegate) {
        if (delegate instanceof WritableByteChannel) {
            return (WritableByteChannel) delegate;
        } else if (delegate instanceof FileOutputStream) {
            final WritableByteChannel ch = ((FileOutputStream) delegate).getChannel();
            if (ch != null) return ch;
        }
        return new IOChannels.OutputStreamChannel(delegate);
    }
