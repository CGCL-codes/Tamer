    public void waitAndWrite(InputStream in, OutputStream out, OutputStream err) throws RuntimeException {
        if (childProcess == null) {
            throw new RuntimeException("Process not yet executed");
        }
        ProcessReader _reader = new ProcessReader(name, childProcess, in, out, err);
        _reader.writeBlocking();
    }
