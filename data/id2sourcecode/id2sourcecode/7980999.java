    public FreePlayerWorker(HttpRequest message, BufferedReader reader, OutputStream writer) {
        this.message = message;
        this.reader = reader;
        this.writer = writer;
    }
