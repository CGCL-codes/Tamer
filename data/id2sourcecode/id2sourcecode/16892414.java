    private static void transmitToFileChannel(final ReadableByteChannel source, final FileChannel destination) throws IOException {
        while (true) {
            final long position = destination.position();
            final long step = destination.transferFrom(source, position, Long.MAX_VALUE);
            if (step <= 0) break;
            destination.position(position + step);
        }
    }
