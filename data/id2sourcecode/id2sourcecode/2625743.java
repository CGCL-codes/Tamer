    private void setupPipes() {
        if (has_pipes) return;
        try {
            in_writer = new PipedWriter();
            in_reader = new BufferedReader(new PipedReader(in_writer));
            out_reader = new PipedReader();
            out_writer = new BufferedWriter(new PipedWriter(out_reader));
            has_pipes = true;
        } catch (IOException ex) {
        }
    }
