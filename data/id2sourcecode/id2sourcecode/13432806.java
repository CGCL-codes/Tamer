    private void parsePsimiStream(InputStream in, long fileLength) throws Exception {
        PSIMIProcessor processor = new OndexPsimiProcessor(graph, this);
        PSIMIEngine engine = new PSIMIEngine(new BufferedInputStream(in), processor);
        if (fileLength > 0) {
            engine.setFileChannel(((FileInputStream) in).getChannel());
            engine.defineStreamLength(fileLength);
        }
        engine.start();
    }
