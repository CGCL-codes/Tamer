    public void sendEOF() throws IOException {
        checkState(!finished, "Cannot write to process that has already terminated.");
        in.close();
    }
