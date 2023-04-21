    public StreamReaderThread(InputStream is, Writer writer) {
        reader = new BufferedReader(new InputStreamReader(is));
        this.writer = new BufferedWriter(writer);
    }
