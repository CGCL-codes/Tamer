    public void close() throws IndexException {
        if (indexFile == null) System.out.println("indexFile already closed.");
        if (!readOnly) writeIndex();
    }
