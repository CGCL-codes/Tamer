    public Pipe() throws IOException {
        writer_ = new PipedWriter();
        reader_ = new PipedReader();
        writer_.connect(reader_);
    }
