    @Nonnull
    public static WritableByteChannel asWritableByteChannel(final DataOutput delegate) {
        if (delegate instanceof WritableByteChannel) return (WritableByteChannel) delegate; else if (delegate instanceof RandomAccessFile) return ((RandomAccessFile) delegate).getChannel(); else if (delegate instanceof OutputStream) return new IOChannels.OutputStreamChannel((OutputStream) delegate); else return new AdapterDataOutput(delegate);
    }
