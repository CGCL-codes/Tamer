    public synchronized void addWriter() {
        if (reader_factory.canMakeWriter() && writer_factory.makeWriter()) {
            addBuffer();
        }
    }
