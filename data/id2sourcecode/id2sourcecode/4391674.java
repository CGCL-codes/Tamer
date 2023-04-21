    public void connect(PipedOutputStream oStrm) throws IOException {
        if (DebugFile.trace) DebugFile.writeln("FTPWorkerThread.connect([PipedOutputStream])");
        oInPipe.connect(oStrm);
    }
