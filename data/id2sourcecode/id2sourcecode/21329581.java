    public static void write(ReadableByteChannel readableChannel, WritableByteChannel writableChannel) throws IOException {
        if ((readableChannel != null) && (writableChannel != null)) {
            write(Channels.newInputStream(readableChannel), Channels.newOutputStream(writableChannel));
        }
    }
