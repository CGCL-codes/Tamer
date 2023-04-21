    NIOSocket(Socket s) throws IOException {
        channel = s.getChannel();
        socket = s;
        writer = new NIOOutputStream(this, channel);
        reader = new NIOInputStream(this, channel);
        writer.init();
        reader.init();
        NIODispatcher.instance().registerReadWrite(channel, this);
        connectedTo = s.getInetAddress();
    }
