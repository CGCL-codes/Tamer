    void writeDEREncoded(OutputStream out, int tag, InputStream in) throws IOException {
        writeDEREncoded(out, tag, Streams.readAll(in));
    }
