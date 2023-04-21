    public CobolBytes map(long position, long size) throws IOException {
        return new CobolBytes(rand.getChannel().map(MapMode.READ_WRITE, position, size).array());
    }
