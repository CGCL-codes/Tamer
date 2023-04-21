    public ServerHandler(Reactor r, SelectableChannel ch) throws IOException, ParserConfigurationException, SAXException {
        super(r, ch);
        this.fromServer = new PipedInputStream();
        this.toThread = new PipedOutputStream(this.fromServer);
        this.toThread.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> <doc>".getBytes());
        this.parser = SAXParserFactory.newInstance().newSAXParser();
    }
