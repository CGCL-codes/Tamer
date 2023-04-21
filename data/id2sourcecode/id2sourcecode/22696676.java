    public void write(File f) throws IOException {
        try {
            MidiSystem.write(seq, type, f);
        } catch (IllegalArgumentException e) {
            IOException ioe = new IOException("Cannot write to the same time the file was read from");
            ioe.initCause(e);
            throw ioe;
        }
    }
