    private static void transmitToOutputByteChannel(final ReadableByteChannel source, final OutputByteChannel destination) throws IOException {
        while (destination.transferFromByteChannel(source, Long.MAX_VALUE) > 0) continue;
    }
