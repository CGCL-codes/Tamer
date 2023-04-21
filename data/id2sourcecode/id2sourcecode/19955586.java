    public void startSession() {
        writer.setStopped(false);
        reader.setStopped(false);
        writerThread = new Thread(writer, "SAWGraphicsModeClientWriter");
        writerThread.setPriority(Thread.NORM_PRIORITY);
        readerThread = new Thread(reader, "SAWGraphicsModeClientReader");
        readerThread.setPriority(Thread.NORM_PRIORITY);
        if (writer.isReadOnly()) {
            SAWTerminal.print("\nSAW>SAWGRAPHICSMODE:Starting graphics mode in view mode...\nSAW>");
        } else {
            SAWTerminal.print("\nSAW>SAWGRAPHICSMODE:Starting graphics mode in control mode...\nSAW>");
        }
        writerThread.start();
        readerThread.start();
    }
