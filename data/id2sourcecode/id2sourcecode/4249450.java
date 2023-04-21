    public ConnectionServiceImpl(String host, int port, String serviceName) throws XMPPException {
        this.serviceName = serviceName;
        try {
            Socket socket = new Socket(host, port);
            InputStreamReader in = new InputStreamReader(socket.getInputStream(), "UTF-8");
            XMLInputFactory inFactory = XMLInputFactory.newInstance();
            reader = inFactory.createXMLStreamReader(in);
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
            eventWriter = outFactory.createXMLEventWriter(out);
            readerThread = new Thread() {

                public void run() {
                    parsePackets(this);
                }
            };
            readerThread.setName("XMPP Packet Reader (" + host + ":" + port + ")");
            readerThread.setDaemon(true);
            writerThread = new Thread() {

                public void run() {
                    writePackets(this);
                }
            };
            writerThread.setName("XMPP Packet Writer (" + host + ":" + port + ")");
            writerThread.setDaemon(true);
            startup();
        } catch (UnknownHostException uhe) {
            String errorMessage = "Could not connect to " + host + ":" + port + ".";
            throw new XMPPException(errorMessage, new XMPPError(XMPPError.Condition.remote_server_timeout, errorMessage), uhe);
        } catch (IOException ioe) {
            String errorMessage = "XMPPError connecting to " + host + ":" + port + ".";
            throw new XMPPException(errorMessage, new XMPPError(XMPPError.Condition.remote_server_error, errorMessage), ioe);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
