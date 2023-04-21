    public void print(String s) throws IOException {
        checkState(!finished, "Cannot write to process that has already terminated.");
        in.write(s);
        in.flush();
    }
