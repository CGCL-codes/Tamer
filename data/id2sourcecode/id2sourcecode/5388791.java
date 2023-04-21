    private void init(Socket s, INode ident, IConnectionListener listener, SocketStreams streams) throws IOException {
        m_socket = s;
        m_localNode = ident;
        m_listener = listener;
        m_socketOut = streams.getSocketOut();
        m_out = m_objectStreamFactory.create(streams.getBufferedOut());
        m_out.writeObject(m_localNode);
        m_out.flush();
        m_socketIn = streams.getSocketIn();
        BufferedInputStream bufferedIn = new BufferedInputStream(streams.getBufferedIn());
        m_in = m_objectStreamFactory.create(bufferedIn);
        try {
            m_remoteNode = (INode) m_in.readObject();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            throw new IllegalStateException("INode class not found");
        }
        m_reader = new Thread(new Reader(), "ConnectionReader for " + m_localNode.getName() + ":" + m_localNode.getAddress());
        m_reader.start();
        m_writer = new Thread(new Writer(), "ConnectionWriter for " + m_localNode.getName() + ":" + m_localNode.getAddress());
        m_writer.start();
    }
