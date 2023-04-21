    public void startSession() {
        reader.setStopped(false);
        writer.setStopped(false);
        readerThread = new Thread(reader, "SAWGraphicsModeServerReader");
        readerThread.setPriority(Thread.NORM_PRIORITY);
        writerThread = new Thread(writer, "SAWGraphicsModeServerWriter");
        writerThread.setPriority(Thread.NORM_PRIORITY);
        writerThread.start();
        readerThread.start();
    }
