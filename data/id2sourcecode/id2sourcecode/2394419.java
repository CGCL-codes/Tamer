    public void run() {
        CommandLineParser parser = new CommandLineParser();
        parser.parseCommandLineParams(_commandLineArgs);
        Selector selector = null;
        try {
            Socket socket = OhuaSocketFactory.getInstance().createSocket(parser._masterIP, parser._port, InetAddress.getLocalHost(), 1222);
            selector = Selector.open();
            socket.getChannel().register(selector, SelectionKey.OP_CONNECT);
            _socketChannel = socket.getChannel();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
