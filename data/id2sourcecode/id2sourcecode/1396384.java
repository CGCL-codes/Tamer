        WriterThread(WriteCallback writer, PipedOutputStream output, String name) {
            super(name);
            this.writer = writer;
            this.output = output;
        }
