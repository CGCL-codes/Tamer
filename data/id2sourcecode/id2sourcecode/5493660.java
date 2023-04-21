    private void startWriter() {
        if (writerThread != null) return;
        Writer writer = new Writer();
        writer.setName(id + ":writer");
        writer.setDaemon(true);
        writer.start();
    }
