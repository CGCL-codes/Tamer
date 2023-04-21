    private void bufferStream(final InputStream i) throws IOException {
        ByteArrayOutputStream sink = new ByteArrayOutputStream(1024);
        while (i.available() > 0) {
            sink.write(i.read());
        }
        i.close();
        this.buffer = sink.toByteArray();
        this.length = buffer.length;
    }
