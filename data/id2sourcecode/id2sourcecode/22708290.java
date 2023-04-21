    private void bufferStream(final InputStream i) throws IOException {
        FastByteArrayOutputStream sink = new FastByteArrayOutputStream();
        while (i.available() > 0) {
            sink.write(i.read());
        }
        i.close();
        sink.close();
        this.buffer = sink.getByteArray();
        this.length = buffer.length;
    }
