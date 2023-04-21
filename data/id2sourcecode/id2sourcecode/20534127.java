    public SocketTraceSource(InetSocketAddress isa) throws IOException {
        if (!FileUtility.cacheAvailable()) {
            throw new IOException("File Cache Not Available: Cannot create socket trace source.");
        }
        String dateStr = makeDateForTemporaryFilename();
        traceFile = FileUtility.getSocketFile("socket-" + dateStr + ".trace");
        FileOutputStream outStream = new FileOutputStream(traceFile);
        outChannel = outStream.getChannel();
        this.isa = isa;
    }
