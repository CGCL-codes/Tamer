    public void connect(PipedInputStream oStrm) throws IOException {
        if (DebugFile.trace) DebugFile.writeln("FTPWorkerThread.connect([PipedInputStream])");
        oOutPipe.connect(oStrm);
    }
