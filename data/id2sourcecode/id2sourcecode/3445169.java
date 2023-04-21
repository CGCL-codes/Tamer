    public synchronized void addReader() {
        if (writer_factory.canMakeReader() && reader_factory.makeReader()) {
            addBuffer();
        }
    }
