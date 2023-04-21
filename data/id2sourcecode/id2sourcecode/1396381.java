    public NullModemInputStream(final WriteCallback writer, String name) {
        super(new PipedInputStream());
        if (writer == null) throw new IllegalArgumentException("null writer");
        try {
            this.output = new PipedOutputStream(this.getPipedInputStream());
        } catch (IOException e) {
            throw new RuntimeException("unexpected exception", e);
        }
        Thread thread = new WriterThread(writer, this.output, name);
        thread.setDaemon(true);
        thread.start();
    }
