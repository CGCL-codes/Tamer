    public OutputStreamXWriter(final File out, final CharsetEncoder encoder) throws IOException {
        this(new FileOutputStream(out).getChannel(), encoder);
        setByteBufferCapacity0((int) Math.min(out.length(), OutputStreamXWriter.DEFAULT_OUTPUT_BUFFER_CAPACITY));
    }
